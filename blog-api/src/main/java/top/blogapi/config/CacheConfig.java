package top.blogapi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        // Default
        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(1000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .recordStats()
        );
        caffeineCacheManager.registerCustomCache(CacheNameConfig.SITE_INFO_MAP, buildCache());
        caffeineCacheManager.registerCustomCache(CacheNameConfig.ABOUT_INFO_MAP, buildCache());
        caffeineCacheManager.registerCustomCache(CacheNameConfig.TAG_CLOUD_LIST, buildCache());
        caffeineCacheManager.registerCustomCache(
                CacheNameConfig.MUSIC_INFO,
                Caffeine.newBuilder()
                        .expireAfterWrite(15, TimeUnit.DAYS)
                        .maximumSize(200)
                        .recordStats()
                        .build()
        );
        return caffeineCacheManager;
    }
    private Cache<Object, Object> buildCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .recordStats()
                .build();
    }
}
