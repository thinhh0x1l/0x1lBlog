package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlogResponse {
    private Long id;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String slug;
    private String description;
    private String coverImage;
    private String contentType;
    private String status;
    private String visibility;
    private Boolean allowComments;
    private Integer words;
    private Integer readTime;
    private Integer views;
    private Integer likeCount;
    private Integer commentCount;
    private Integer bookmarkCount;
    private Integer shareCount;
    private OffsetDateTime publishedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
