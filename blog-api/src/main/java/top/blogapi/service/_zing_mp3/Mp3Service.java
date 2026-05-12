package top.blogapi.service._zing_mp3;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Mp3Service {

    ZingMp3Client zingMp3Client;

    public void setConfigFromDb(Map<String, Map<String, Object>> configMap) {
        zingMp3Client.setConfig(configMap);
    }


    public boolean isConfigured() {
        return zingMp3Client.hasConfig();
    }

    public Map<String, String> getSongInfo(String songId) {
        JsonNode info = zingMp3Client.getSongInfo(songId);
        return Map.of(
                "title", info.at("/data/title").asText(),
                "name", info.at("/data/artists/0/name").asText(),
                "thumbnail", info.at("/data/thumbnailM").asText()
        );
    }

    public void reLoadRestClient(){
        zingMp3Client.loadRestClient();
        zingMp3Client.setForceRefresh(true);
        System.out.println("ReLoadMpe3");
    }

    public String getSongStreaming(String songId) {
        return zingMp3Client.getSongStreaming(songId).at("/data/128").asText();
    }

    public String getLyric(String songId) throws Exception {
        JsonNode lyric = zingMp3Client.getLyric(songId);
        return lyric.at("/data/file").asText();
    }
}