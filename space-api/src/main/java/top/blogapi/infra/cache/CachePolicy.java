package top.blogapi.infra.cache;

import java.time.Duration;

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
