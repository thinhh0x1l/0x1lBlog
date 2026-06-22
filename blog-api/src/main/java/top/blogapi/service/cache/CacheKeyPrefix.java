package top.blogapi.service.cache;

public final class CacheKeyPrefix {

    private CacheKeyPrefix() {}

    public static final String BLOG_ENTITY = "blog:entity:";
    public static final String BLOG_LIST = "blog:list:";
    public static final String BLOG_FEED = "blog:feed:";
    public static final String BLOG_TRENDING = "blog:trending:";
    public static final String BLOG_STATS = "blog:stats:";

    public static final String USER_ENTITY = "user:entity:";
    public static final String CATEGORY_ENTITY = "category:entity:";
    public static final String CATEGORY_LIST = "category:list:";
    public static final String TAG_ENTITY = "tag:entity:";
    public static final String TAG_LIST = "tag:list:";

    public static String blogEntity(Long id) {
        return BLOG_ENTITY + id;
    }

    public static String blogSlug(String slug) {
        return BLOG_ENTITY + "slug:" + slug;
    }

    public static String userList(int page, int size) {
        return BLOG_LIST + "user:" + page + ":" + size;
    }

    public static String categoryList() {
        return CATEGORY_LIST + "all";
    }

    public static String tagList() {
        return TAG_LIST + "all";
    }
}
