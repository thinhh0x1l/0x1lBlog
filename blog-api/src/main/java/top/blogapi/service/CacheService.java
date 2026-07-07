package top.blogapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import top.blogapi.service.cache.CacheMetrics;
import top.blogapi.service.cache.CachePolicy;
import top.blogapi.service.cache.CacheRegion;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Entry-point duy nhất cho cache.
 * <p>
 * Mỗi domain inject {@code CacheService} và chọn {@link CachePolicy} phù hợp.
 * Không dùng Spring {@code @Cacheable} — key mismatch và stale data.
 * <p>
 * Cache layers:
 * <ul>
 *   <li>L1: Caffeine (in-process, striped-lock stampede protection + double-check)</li>
 *   <li>L2: Redis (JSON serialization via ObjectMapper)</li>
 *   <li>DB: Supplier fallback khi miss cả 2 tầng</li>
 * </ul>
 */
public interface CacheService {
    <T> T get(String key, Class<T> clazz, Supplier<T> loader, CachePolicy policy);

    <T> T get(String key, TypeReference<T> type, Supplier<T> loader, CachePolicy policy);

    void put(String key, Object value, CachePolicy policy);

    void evict(String key);

    void evictAll(Collection<String> keys);

    void evictRegion(CacheRegion region);

    CacheMetrics metrics();
}
