package top.blogapi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.blogapi.service.cache.CacheMetrics;

import java.time.Duration;

/**
 * Caffeine cache beans cho CacheServiceImpl (L1).
 * <p>
 * L1: 10k entries, 30 phút max TTL. Dùng chung cho tất cả domain.
 */
@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .build();
    }

    @Bean
    public CacheMetrics cacheMetrics() {
        return new CacheMetrics();
    }
}
