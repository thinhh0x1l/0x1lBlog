package top.blogapi.dto.internal;

import lombok.Value;

/**
 * DTO nội bộ chứa dữ liệu chi tiết blog để truyền giữa các service.
 */
@Value
public class BlogDetailInternal {
    Long id;
    String title;
    String slug;
    String content;
    String description;
    String coverImage;
    String authorName;
    String authorAvatar;
    String categoryName;
    Integer views;
    Integer likeCount;
    Integer commentCount;
}
