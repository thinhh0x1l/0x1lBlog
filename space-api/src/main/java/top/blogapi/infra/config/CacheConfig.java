package top.blogapi.infra.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.blogapi.infra.cache.CacheMetrics;

import java.time.Duration;

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
