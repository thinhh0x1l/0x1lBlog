package top.blogapi.service.blog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.blogapi.service.cache.CacheKeyPrefix;

import java.util.concurrent.TimeUnit;

@Service
public class BlogCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Cache<String, Object> l1;

    private static final long L1_TTL_MINUTES = 5;
    private static final long L2_TTL_MINUTES = 30;

    public BlogCacheService(@Qualifier("redisObjectTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.l1 = Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(L1_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    public <T> T get(String key, TypeReference<T> typeRef) {
        Object cached = l1.getIfPresent(key);
        if (cached != null) {
            @SuppressWarnings("unchecked")
            T result = (T) cached;
            return result;
        }

        Object cached2 = redisTemplate.opsForValue().get(key);
        if (cached2 != null) {
            l1.put(key, cached2);
            @SuppressWarnings("unchecked")
            T result = (T) cached2;
            return result;
        }

        return null;
    }

    public void put(String key, Object value) {
        l1.put(key, value);
        redisTemplate.opsForValue().set(key, value, L2_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public void evict(String key) {
        l1.invalidate(key);
        redisTemplate.delete(key);
    }

    public void evictBlog(Long blogId) {
        evict(CacheKeyPrefix.blogEntity(blogId));
    }
}
