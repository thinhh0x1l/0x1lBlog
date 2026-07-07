package top.blogapi.service.cache;

import java.time.Duration;

/**
 * Cache policy cho từng domain.
 * <p>
 * Mỗi domain quyết định chiến lược riêng: dùng L1, L2, cache null (chống penetration), TTL.
 * <p>
 * Factory methods: {@link #both(Duration)}, {@link #local(Duration)}, {@link #none()}.
 */
public record CachePolicy(
        boolean useLocal,
        boolean useRedis,
        boolean cacheNull,
        Duration ttl
) {
    public static CachePolicy both(Duration ttl) {
        return new CachePolicy(true, true, true, ttl);
    }

    public static CachePolicy bothNoNull(Duration ttl) {
        return new CachePolicy(true, true, false, ttl);
    }

    public static CachePolicy local(Duration ttl) {
        return new CachePolicy(true, false, true, ttl);
    }

    public static CachePolicy localNoNull(Duration ttl) {
        return new CachePolicy(true, false, false, ttl);
    }

    public static CachePolicy none() {
        return new CachePolicy(false, false, false, Duration.ZERO);
    }
}
