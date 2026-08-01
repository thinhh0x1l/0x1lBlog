package top.blogapi.infra.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Service
public class CacheServiceImpl implements CacheService {

    private static final Object NULL_PLACEHOLDER = new Object();
    private static final Duration NULL_TTL = Duration.ofSeconds(30);
    private static final int STRIPE_COUNT = 512;

    private final Cache<String, Object> caffeine;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final CacheMetrics metrics;
    private final Lock[] stripes;

    public CacheServiceImpl(
            Cache<String, Object> caffeine,
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper,
            CacheMetrics metrics
    ) {
        this.caffeine = caffeine;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.metrics = metrics;
        this.stripes = new Lock[STRIPE_COUNT];
        for (int i = 0; i < STRIPE_COUNT; i++) {
            stripes[i] = new ReentrantLock();
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz, Supplier<T> loader, CachePolicy policy) {
        if (!policy.useLocal() && !policy.useRedis()) {
            return loader.get();
        }

        if (policy.useLocal()) {
            Object local = caffeine.getIfPresent(key);
            if (local == NULL_PLACEHOLDER) {
                metrics.l1Hit().incrementAndGet();
                return null;
            }
            if (local != null) {
                metrics.l1Hit().incrementAndGet();
                return clazz.cast(local);
            }
        }
        metrics.l1Miss().incrementAndGet();

        if (policy.useRedis()) {
            String json = redisTemplate.opsForValue().get(key);
            if (json != null) {
                if (json.isEmpty()) {
                    metrics.l2Hit().incrementAndGet();
                    if (policy.useLocal()) {
                        caffeine.put(key, NULL_PLACEHOLDER);
                    }
                    return null;
                }
                metrics.l2Hit().incrementAndGet();
                T value = deserialize(json, clazz);
                if (value != null && policy.useLocal()) {
                    caffeine.put(key, value);
                }
                return value;
            }
        }
        metrics.l2Miss().incrementAndGet();

        Lock lock = stripe(key);
        lock.lock();
        try {
            if (policy.useLocal()) {
                Object local = caffeine.getIfPresent(key);
                if (local == NULL_PLACEHOLDER) {
                    return null;
                }
                if (local != null) {
                    return clazz.cast(local);
                }
            }

            metrics.dbHit().incrementAndGet();
            T value = loader.get();

            if (value == null) {
                if (policy.cacheNull()) {
                    if (policy.useLocal()) {
                        caffeine.put(key, NULL_PLACEHOLDER);
                    }
                    if (policy.useRedis()) {
                        redisTemplate.opsForValue().set(key, "", NULL_TTL.getSeconds(), TimeUnit.SECONDS);
                    }
                }
                return null;
            }

            if (policy.useLocal()) {
                caffeine.put(key, value);
            }
            if (policy.useRedis() && policy.ttl() != null && !policy.ttl().isZero()) {
                String serialized = serialize(value);
                if (serialized != null) {
                    redisTemplate.opsForValue().set(key, serialized, policy.ttl().getSeconds(), TimeUnit.SECONDS);
                }
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T get(String key, TypeReference<T> type, Supplier<T> loader, CachePolicy policy) {
        if (!policy.useLocal() && !policy.useRedis()) {
            return loader.get();
        }

        if (policy.useLocal()) {
            Object local = caffeine.getIfPresent(key);
            if (local == NULL_PLACEHOLDER) {
                metrics.l1Hit().incrementAndGet();
                return null;
            }
            if (local != null) {
                metrics.l1Hit().incrementAndGet();
                @SuppressWarnings("unchecked")
                T result = (T) local;
                return result;
            }
        }
        metrics.l1Miss().incrementAndGet();

        if (policy.useRedis()) {
            String json = redisTemplate.opsForValue().get(key);
            if (json != null) {
                if (json.isEmpty()) {
                    metrics.l2Hit().incrementAndGet();
                    if (policy.useLocal()) {
                        caffeine.put(key, NULL_PLACEHOLDER);
                    }
                    return null;
                }
                metrics.l2Hit().incrementAndGet();
                T value = deserialize(json, type);
                if (value != null && policy.useLocal()) {
                    caffeine.put(key, value);
                }
                return value;
            }
        }
        metrics.l2Miss().incrementAndGet();

        Lock lock = stripe(key);
        lock.lock();
        try {
            if (policy.useLocal()) {
                Object local = caffeine.getIfPresent(key);
                if (local == NULL_PLACEHOLDER) {
                    return null;
                }
                if (local != null) {
                    @SuppressWarnings("unchecked")
                    T result = (T) local;
                    return result;
                }
            }

            metrics.dbHit().incrementAndGet();
            T value = loader.get();

            if (value == null) {
                if (policy.cacheNull()) {
                    if (policy.useLocal()) {
                        caffeine.put(key, NULL_PLACEHOLDER);
                    }
                    if (policy.useRedis()) {
                        redisTemplate.opsForValue().set(key, "", NULL_TTL.getSeconds(), TimeUnit.SECONDS);
                    }
                }
                return null;
            }

            if (policy.useLocal()) {
                caffeine.put(key, value);
            }
            if (policy.useRedis() && policy.ttl() != null && !policy.ttl().isZero()) {
                String serialized = serialize(value);
                if (serialized != null) {
                    redisTemplate.opsForValue().set(key, serialized, policy.ttl().getSeconds(), TimeUnit.SECONDS);
                }
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(String key, Object value, CachePolicy policy) {
        if (value == null) return;
        if (policy.useLocal()) {
            caffeine.put(key, value);
        }
        if (policy.useRedis() && policy.ttl() != null && !policy.ttl().isZero()) {
            String serialized = serialize(value);
            if (serialized != null) {
                redisTemplate.opsForValue().set(key, serialized, policy.ttl().getSeconds(), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void evict(String key) {
        caffeine.invalidate(key);
        redisTemplate.delete(key);
    }

    @Override
    public void evictAll(Collection<String> keys) {
        keys.forEach(caffeine::invalidate);
        redisTemplate.delete(keys);
    }

    @Override
    public void evictRegion(CacheRegion region) {
        String prefix = region.keyPrefix();
        caffeine.asMap().keySet().removeIf(k -> k.startsWith(prefix));
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public CacheMetrics metrics() {
        return metrics;
    }

    private Lock stripe(String key) {
        return stripes[Math.floorMod(key.hashCode(), STRIPE_COUNT)];
    }

    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T deserialize(String json, TypeReference<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }
}
