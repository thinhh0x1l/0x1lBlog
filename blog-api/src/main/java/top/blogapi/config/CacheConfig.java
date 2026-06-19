package top.blogapi.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.blogapi.constant.CacheNameConstant;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager caffeineCacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        caffeineCacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(100)
                        .maximumSize(1000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .recordStats()
        );
        caffeineCacheManager.registerCustomCache(CacheNameConstant.SITE_INFO_MAP, buildCache());
        caffeineCacheManager.registerCustomCache(CacheNameConstant.ABOUT_INFO_MAP, buildCache());
        caffeineCacheManager.registerCustomCache(CacheNameConstant.TAG_CLOUD_LIST, buildCache());
        caffeineCacheManager.registerCustomCache(
                CacheNameConstant.MUSIC_INFO,
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

    @Bean
    public Cache<Long, AtomicLong> blogViewCache() {
        return Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfterAccess(Duration.ofHours(1))
                .build();
    }
}
