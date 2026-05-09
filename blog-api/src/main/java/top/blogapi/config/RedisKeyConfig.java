package top.blogapi.config;

public class RedisKeyConfig {

    // Danh sách blog giới thiệu trang chủ - key phân trang:
    // homeBlogInfoList : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String HOME_BLOG_INFO_LIST = "homeBlogInfoList";

    // Danh sách blog giới thiệu theo tên danh mục - tiền tố key phân trang:
    // categoryBlogInfoList_ : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String CATEGORY_BLOG_INFO_LIST = "categoryBlogInfoList_";

    // Danh sách blog giới thiệu theo tên thẻ - tiền tố key phân trang:
    // tagBlogInfoList_ : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String TAG_BLOG_INFO_LIST = "tagBlogInfoList_";

    public static final String CATEGORY_NAME_LIST = "categoryNameList";

    public static final String TAG_CLOUD_LIST = "tagCloudList";

    public static final String SITE_INFO_MAP = "siteInfoMap";

    public static final String NEW_BLOG_LIST = "newBlogList";

    public static final String ABOUT_INFO_MAP = "aboutInfoMap";

    public static final String ARCHIVE_BLOG_MAP = "archiveBlogMap";

}
