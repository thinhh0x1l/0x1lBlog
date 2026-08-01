package top.blogapi.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class IpAddressUtils {
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IPV6_SHORT = "::1";

    public static final List<String> IP_HEADERS = List.of(
            "X-Forwarded-For",        // Proxy true
            "CF-Connecting-IP",        // Cloudflare
            "X-Real-IP",              // Nginx
            "True-Client-IP",         // Akamai, Cloudflare
            "Proxy-Client-IP",        // Proxy cũ
            "WL-Proxy-Client-IP",     // WebLogic
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );

    /**
     * Lấy địa chỉ IP thật của client
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null)
            return UNKNOWN;
        String ip = getIpFromHeaders(request);
        if (!isValidIp(ip))
            ip = request.getRemoteAddr();
        if (isLocalhost(ip))
            ip = getLocalMachineIp();
        ip = extractFirstIp(ip);

        log.debug("Client IP resolved: {}", ip);
        return ip;
    }

    /**
     * Lấy IP từ headers
     */
    private static String getIpFromHeaders(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (isValidIp(ip)) {
                log.debug("Tìm thấy IP tại header {}: {}", header, ip);
                return ip;
            }
        }
        return null;
    }

    /**
     * Kiểm tra IP hợp lệ
     */
    public static boolean isValidIp(String ip) {
        if (!StringUtils.hasText(ip)) return false;
        if (UNKNOWN.equalsIgnoreCase(ip)) return false;
        ip = ip.trim();
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy IP đầu tiên từ chuỗi
     * Xử lý: "client, proxy1, proxy2" hoặc "192.168.1.1:8080"
     */
    public static String extractFirstIp(String ip) {
        if (!StringUtils.hasText(ip)) return ip;
        // X-Forwarded-For có thể dạng: "client, proxy1, proxy2"
        if (ip.contains(","))
            ip = ip.split(",")[0].trim();
        // Có thể dạng: "1.2.3.4:8080" hoặc "192.168.1.1:8080"
        if (ip.contains(":")) {
            // Đếm số dấu : để phân biệt IPv4:port vs IPv6
            long colonCount = ip.chars().filter(ch -> ch == ':').count();
            // IPv4:port
            if (colonCount == 1)
                ip = ip.split(":")[0];
            // IPv6 có nhiều dấu : -> giữ nguyên
        }
        return ip;
    }

    /**
     * Kiểm tra localhost
     */
    public static boolean isLocalhost(String ip) {
        if (!StringUtils.hasText(ip)) return false;
        ip = ip.trim();
        return LOCALHOST_IPV4.equals(ip)
                || LOCALHOST_IPV6.equals(ip)
                || LOCALHOST_IPV6_SHORT.equals(ip)
                || "localhost".equalsIgnoreCase(ip)
                || "0.0.0.0".equals(ip);
    }

    /**
     * Lấy IP máy thật
     */
    private static String getLocalMachineIp() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Không thể resolve localhost IP, 127.0.0.1", e);
            return LOCALHOST_IPV4;
        }
    }

    /**
     * Kiểm tra IP có phải private network không
     * 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16
     */
    public static boolean isPrivateIp(String ip) {
        if (!StringUtils.hasText(ip)) return false;

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isSiteLocalAddress()   // private network
                    || address.isLoopbackAddress() // localhost
                    || address.isLinkLocalAddress(); // 169.254.x.x
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Che giấu IP khi log (cho visitor)
     */
    public static String maskIpForLog(String ip) {
        if (!StringUtils.hasText(ip) || ip.length() < 8)
            return ip;

        // IPv4: 192.168.1.100 -> 192.168.1.***
        int lastDot = ip.lastIndexOf('.');
        if (lastDot > 0)
            return ip.substring(0, lastDot + 1) + "***";

        // IPv6: 2001:db8::123 -> 2001:db8::***
        int lastColon = ip.lastIndexOf(':');
        if (lastColon > 0)
            return ip.substring(0, lastColon + 1) + "***";
        return ip;
    }


    public static void main(String[] args) throws Exception {

        try (InputStream inputStream = IpAddressUtils.class.getClassLoader()
                .getResourceAsStream("ipdb/City.mmdb");
             DatabaseReader reader = new DatabaseReader.Builder(inputStream).build()

            ){
            String ip = "14.186.68.109";

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            System.out.println(response.city());
            System.out.println("Country: " + response.country().name());
            System.out.println("Country: " + response.country().names());
            System.out.println("ISO Code: " + response.country().isoCode());
            System.out.println("City: " + response.city().name());
            System.out.println("City: " + response.city().names());
            System.out.println("Province: " + response.mostSpecificSubdivision().name());
            System.out.println("Province: " + response.mostSpecificSubdivision().names());
            System.out.println("Latitude: " + response.location().latitude());
            System.out.println("Longitude: " + response.location().longitude());
            System.out.println("ISP/ASN: (not in City DB)");

            URL asnFile = IpAddressUtils.class.getClassLoader().getResource("ipdb/ASN.mmdb");

            DatabaseReader asnReader = new DatabaseReader.Builder(new File(asnFile.getFile())).build();
            AsnResponse asn = asnReader.asn(ipAddress);

            System.out.println(asn.autonomousSystemOrganization());
            System.out.println(asn.autonomousSystemNumber());
        }

    }
}