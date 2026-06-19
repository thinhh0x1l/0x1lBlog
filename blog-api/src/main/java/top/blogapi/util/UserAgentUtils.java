package top.blogapi.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserAgentUtils {

    public record UserAgentInfo(String os, String browser) {
        public static final UserAgentInfo UNKNOWN = new UserAgentInfo("Unknown", "Unknown");
    }

    private final UserAgentAnalyzer uaa;
    private final Cache<String, UserAgentInfo> resultCache;

    public UserAgentUtils() {
        this.uaa = UserAgentAnalyzer
                .newBuilder()
                .withCache(10000)
                .hideMatcherLoadStats()
                .withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION)
                .withField(UserAgent.AGENT_NAME_VERSION)
                .build();

        this.resultCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.DAYS)
                .expireAfterAccess(60, TimeUnit.DAYS)
                .recordStats()
                .build();
    }

    public UserAgentInfo parseOsAndBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return UserAgentInfo.UNKNOWN;
        }

        UserAgentInfo cached = resultCache.getIfPresent(userAgent);
        if (cached != null) {
            return cached;
        }

        try {
            UserAgent agent = uaa.parse(userAgent);
            String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION);
            String browser = agent.getValue(UserAgent.AGENT_NAME_VERSION);

            UserAgentInfo result = new UserAgentInfo(
                    os != null ? os : "Unknown",
                    browser != null ? browser : "Unknown"
            );

            resultCache.put(userAgent, result);
            return result;
        } catch (Exception e) {
            log.warn("Failed to parse User-Agent: {}", userAgent, e);
            return UserAgentInfo.UNKNOWN;
        }
    }

    public double getHitRate() {
        return resultCache.stats().hitRate();
    }
}
