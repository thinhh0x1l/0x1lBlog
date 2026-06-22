package top.blogapi.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EntityCacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisObjectTemplate;

    private static final long ENTITY_TTL_MINUTES = 30;
    private static final long LIST_TTL_MINUTES = 6;

    public <T> Optional<T> get(String cacheName, Object key, Class<T> type) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return Optional.empty();
        return Optional.ofNullable(cache.get(key, type));
    }

    public void put(String cacheName, Object key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) cache.put(key, value);
    }

    public void evict(String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) cache.evict(key);
    }

    public void evictAll(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) cache.clear();
    }

    public <T> T getOrLoad(String cacheName, Object key, Class<T> type, Supplier<T> loader) {
        return get(cacheName, key, type).orElseGet(() -> {
            T value = loader.get();
            if (value != null) {
                put(cacheName, key, value);
            }
            return value;
        });
    }
}
