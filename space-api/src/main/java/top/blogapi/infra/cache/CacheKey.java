package top.blogapi.infra.cache;

public final class CacheKey {
    private CacheKey() {}

    public static String blog(Long id) { return "blog:" + id; }
    public static String blogSlug(String slug) { return "blog:slug:" + slug; }
    public static String user(Long id) { return "user:" + id; }
    public static String category(Long id) { return "category:" + id; }
    public static String categoryBySlug(String slug) { return "category:slug:" + slug; }
    public static String tag(Long id) { return "tag:" + id; }
    public static String comment(Long id) { return "comment:" + id; }
    public static String series(Long id) { return "series:" + id; }
    public static String status(Long id) { return "status:" + id; }
    public static String story(Long id) { return "story:" + id; }
    public static String playlist(Long id) { return "playlist:" + id; }
    public static String canvas(Long id) { return "canvas:" + id; }
    public static String quest(Long id) { return "quest:" + id; }
    public static String skill(Long id) { return "skill:" + id; }
    public static String profileWidget(Long userId) { return "profile:widget:" + userId; }
    public static String reactionCount(String targetType, Long targetId) { return "reaction:count:" + targetType + ":" + targetId; }
    public static String siteSettingAll() { return "site:settings:all"; }
    public static String siteSettingByKey(String key) { return "site:settings:key:" + key; }
    public static String notificationByUser(Long userId, int page, int size) { return "notification:user:" + userId + ":" + page + ":" + size; }
    public static String notificationUnread(Long userId) { return "notification:unread:" + userId; }
}
