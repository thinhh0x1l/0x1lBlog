package top.blogapi.service.cache;

/**
 * Region cho {@link CacheService#evictRegion(CacheRegion)}.
 * <p>
 * Khi gọi evictRegion, cache service xoá tất cả keys có prefix tương ứng
 * khỏi cả L1 (Caffeine) và L2 (Redis).
 */
public enum CacheRegion {
    BLOG("blog:"),
    USER_PROFILE("user:"),
    CATEGORY("category:"),
    TAG("tag:"),
    SERIES("series:"),
    SITE_SETTING("site:settings:");

    private final String keyPrefix;

    CacheRegion(String keyPrefix) { this.keyPrefix = keyPrefix; }

    public String keyPrefix() { return keyPrefix; }
}
