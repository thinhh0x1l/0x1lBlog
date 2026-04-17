package top.blogapi.client.zing_mp3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ZingMp3Client {
    @Setter
    boolean forceRefresh;
    String version;
    String secretKey;
    String apiKey;
    RestClient restClient;
    String userAgent;
    Map<String, Object> mp3k, mp3h;
    final ObjectMapper objectMapper;
    final Map<String, String> cookieCache = new ConcurrentHashMap<>();
    boolean configured = false;

    public ZingMp3Client(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.forceRefresh = false;
    }


    public void setConfig(Map<String, Map<String, Object>> configMap) {
        if (configMap == null) {
            throw new IllegalArgumentException("Config map không được null");
        }
        mp3k = configMap.get("mp3-k");
        if (mp3k == null) {
            throw new IllegalArgumentException("Thiếu config 'mp3-k'");
        }

        this.apiKey = getRequiredValue(mp3k, "API_KEY");
        this.secretKey = getRequiredValue(mp3k, "SECRET_KEY");
        this.version = getRequiredValue(mp3k, "VERSION");

        mp3h = configMap.get("mp3-h");

        loadRestClient();

        cookieCache.clear();
        configured = true;
    }

    public void loadRestClient(){
        if (mp3h != null && !mp3h.isEmpty()) {
            userAgent=mp3h.get("User-Agent").toString();
            this.restClient = createRestClient(mp3h);
        } else {
            throw new IllegalArgumentException("Thiếu config 'mp3-h' cho headers");
        }

    }

    private String getRequiredValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu giá trị cho: " + key);
        }
        return value.toString();
    }

    private RestClient createRestClient(Map<String, Object> headers) {
        RestClient.Builder builder = RestClient.builder();
        if (headers != null && !headers.isEmpty()) {
            builder.defaultHeaders(httpHeader -> {
                headers.forEach((key, value) -> {
                    if (value != null) {
                        httpHeader.set(key, value.toString());
                    }
                });
            });
        }
        return builder.build();
    }


    public boolean hasConfig() {
        return configured && apiKey != null && secretKey != null && version != null && restClient != null;
    }


    public JsonNode getSongInfo(String songId) {
        checkConfig();
        return callZingApi("info", songId);
    }

    public JsonNode getSongStreaming(String songId) {
        checkConfig();
        return callZingApi("streaming", songId);
    }

    public JsonNode getLyric(String songId) {
        checkConfig();
        return callZingApi("lyric", songId);
    }


    private void checkConfig() {
        if (!hasConfig()) {
            throw new IllegalStateException("Chưa cấu hình ZingMp3Client");
        }
    }

    private JsonNode callZingApi(String apiType, String songId) {
        try {
            String cookies = getCookies();
            // zmp3_app_version.1=11713;
            // zmp3_rqid=MHwxNC4xODYdUngMTI4LjI5fHYxLjE3LjEzfDE3NzYwMTM0OTmUsICzMTE;
            System.out.println(cookies);
            String ctime = String.valueOf(System.currentTimeMillis() / 1000);
            String hashInput = "ctime=" + ctime + "id=" + songId + "version=" + version;
            String hash256 = getSHA256(hashInput);

            String endpoint = switch (apiType) {
                case "info" -> "/api/v2/song/get/info";
                case "streaming" -> "/api/v2/song/get/streaming";
                case "lyric" -> "/api/v2/lyric/get/lyric";
                default -> throw new IllegalArgumentException("API không hợp lệ: " + apiType);
            };

            String sig = getHMACSHA512(endpoint + hash256, secretKey);

            String url = "https://zingmp3.vn" + endpoint +
                    "?id=" + songId +
                    "&ctime=" + ctime +
                    "&version=" + version +
                    "&apiKey=" + apiKey +
                    "&sig=" + sig;

            String response = restClient.get()
                    .uri(url)
                    .header("Cookie", cookies)
                    .retrieve()
                    .body(String.class);

            JsonNode json = objectMapper.readTree(response);

            if (json.has("err") && json.get("err").asInt() != 0) {
                log.error("json log {}",json);
                throw new RuntimeException("Zing API bị chặn: " + json.toString());
            }

            return json;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Gọi ZingMP3 API thất bại: " + apiType, e);
        }
    }

    private String getCookies() {
        if (forceRefresh) {
            cookieCache.remove("zing_cookies");
            forceRefresh = false;
            log.info("remove zing_cookies");
        }
        return cookieCache.computeIfAbsent("zing_cookies", key -> {
            try {
                Document doc = Jsoup.connect("https://zingmp3.vn")
                        .userAgent(userAgent)
//                        .header("Connection", "keep-alive")
                        .timeout(10000)
                        .get();

                Map<String, String> cookies = doc.connection().response().cookies();
                StringBuilder cookieString = new StringBuilder();
                cookies.forEach((name, value) ->
                        cookieString.append(name).append("=").append(value).append("; ")
                );

                return cookieString.toString();
            } catch (Exception e) {
                throw new RuntimeException("Lấy cookies thất bại", e);
            }
        });
    }


    private String getSHA256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return getHexString(hash);
    }

    private String getHMACSHA512(String data, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKey);
        byte[] result = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return getHexString(result);
    }

    @NonNull
    private String getHexString(byte[] result) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : result) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}