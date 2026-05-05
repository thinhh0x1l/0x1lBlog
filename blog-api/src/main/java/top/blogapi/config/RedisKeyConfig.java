package top.blogapi.config;

public class RedisKeyConfig {

    // Danh sách blog giới thiệu trang chủ - key phân trang:
    // homeBlogInfoList : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String HOME_BLOG_INFO_LIST = "homeBlogInfoList";

    // Danh sách blog giới thiệu theo tên danh mục - tiền tố key phân trang:
    // categoryBlogInfoList_ : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String Category_BLOG_INFO_LIST = "categoryBlogInfoList_";

    // Danh sách blog giới thiệu theo tên thẻ - tiền tố key phân trang:
    // tagBlogInfoList_ : {{1,"bộ nhớ đệm trang 1"},{2,"bộ nhớ đệm trang 2"}}
    public static final String Tag_BLOG_INFO_LIST = "tagBlogInfoList_";
}
