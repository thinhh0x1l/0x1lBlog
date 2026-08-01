package top.blogapi.infra.cache;

import java.time.Duration;

public final class CachePolicies {
    private CachePolicies() {}

    public static final CachePolicy BLOG = CachePolicy.both(Duration.ofMinutes(30));
    public static final CachePolicy BLOG_SLUG = CachePolicy.both(Duration.ofMinutes(30));
    public static final CachePolicy USER_PROFILE = CachePolicy.both(Duration.ofMinutes(10));
    public static final CachePolicy CATEGORY = CachePolicy.local(Duration.ofHours(1));
    public static final CachePolicy TAG = CachePolicy.local(Duration.ofHours(6));
    public static final CachePolicy SERIES = CachePolicy.local(Duration.ofMinutes(30));
    public static final CachePolicy SITE_SETTING = CachePolicy.both(Duration.ofHours(6));
}
