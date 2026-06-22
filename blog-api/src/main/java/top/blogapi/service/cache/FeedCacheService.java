package top.blogapi.service.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class FeedCacheService {

    private final RedisTemplate<String, Object> redisObjectTemplate;

    private static final long FEED_TTL_SECONDS = 30;
    private static final long TRENDING_TTL_SECONDS = 60;
    private static final long SEARCH_TTL_SECONDS = 30;
    private static final long LIST_TTL_SECONDS = 360;

    public <T> T get(String key, TypeReference<T> typeRef) {
        Object val = redisObjectTemplate.opsForValue().get(key);
        if (val == null) return null;
        @SuppressWarnings("unchecked")
        T result = (T) val;
        return result;
    }

    public void put(String key, Object value, long ttlSeconds) {
        redisObjectTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    public void evict(String key) {
        redisObjectTemplate.delete(key);
    }

    public void evictPattern(String pattern) {
        var keys = redisObjectTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisObjectTemplate.delete(keys);
        }
    }

    public <T> T getOrLoadFeed(String key, TypeReference<T> typeRef, Supplier<T> loader) {
        T cached = get(key, typeRef);
        if (cached != null) return cached;
        T value = loader.get();
        if (value != null) {
            put(key, value, FEED_TTL_SECONDS);
        }
        return value;
    }

    public <T> T getOrLoadTrending(String key, TypeReference<T> typeRef, Supplier<T> loader) {
        T cached = get(key, typeRef);
        if (cached != null) return cached;
        T value = loader.get();
        if (value != null) {
            put(key, value, TRENDING_TTL_SECONDS);
        }
        return value;
    }

    public <T> T getOrLoadSearch(String key, TypeReference<T> typeRef, Supplier<T> loader) {
        T cached = get(key, typeRef);
        if (cached != null) return cached;
        T value = loader.get();
        if (value != null) {
            put(key, value, SEARCH_TTL_SECONDS);
        }
        return value;
    }

    public <T> T getOrLoadList(String key, TypeReference<T> typeRef, Supplier<T> loader) {
        T cached = get(key, typeRef);
        if (cached != null) return cached;
        T value = loader.get();
        if (value != null) {
            put(key, value, LIST_TTL_SECONDS);
        }
        return value;
    }
}
