package top.blogapi.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;
import top.blogapi.dto.internal.UserAgentDTO;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserAgentUtils {

    private final UserAgentAnalyzer uaa;
    private final Cache<String, UserAgentDTO> resultCache;

    public UserAgentUtils() {
        this.uaa = UserAgentAnalyzer
                .newBuilder()
                .useJava8CompatibleCaching()
                .withCache(10000)
                .hideMatcherLoadStats()
                .withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION)
                .withField(UserAgent.AGENT_NAME_VERSION)
                .build();

        this.resultCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.DAYS)    // 30 ngày (hợp lý)
                .expireAfterAccess(60, TimeUnit.DAYS)    // Không dùng 7 ngày → xóa
                .recordStats()
                .build();
    }

    public UserAgentDTO parseOsAndBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return UserAgentDTO.UNKNOWN;
        }

        UserAgentDTO cached = resultCache.getIfPresent(userAgent);
        if (cached != null) {
            return cached;
        }

        try {
            UserAgent agent = uaa.parse(userAgent);
            String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION);
            String browser = agent.getValue(UserAgent.AGENT_NAME_VERSION);

            UserAgentDTO result = new UserAgentDTO(
                    os != null ? os : "Unknown",
                    browser != null ? browser : "Unknown"
            );

            resultCache.put(userAgent, result);
            return result;
        } catch (Exception e) {
            log.warn("Failed to parse User-Agent: {}", userAgent, e);
            return UserAgentDTO.UNKNOWN;
        }
    }

    public double getHitRate() {
        return resultCache.stats().hitRate();
    }
}