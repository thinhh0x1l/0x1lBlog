package top.blogapi.infra.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collection;
import java.util.function.Supplier;

public interface CacheService {
    <T> T get(String key, Class<T> clazz, Supplier<T> loader, CachePolicy policy);

    <T> T get(String key, TypeReference<T> type, Supplier<T> loader, CachePolicy policy);

    void put(String key, Object value, CachePolicy policy);

    void evict(String key);

    void evictAll(Collection<String> keys);

    void evictRegion(CacheRegion region);

    CacheMetrics metrics();
}
