package top.blogapi.dto.internal;

import lombok.Value;

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
