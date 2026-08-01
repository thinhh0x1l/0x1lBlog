package top.blogapi.infra.cache;

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
