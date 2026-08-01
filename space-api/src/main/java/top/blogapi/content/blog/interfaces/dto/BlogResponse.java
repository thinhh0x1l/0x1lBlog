package top.blogapi.content.blog.interfaces.dto;

import java.time.Instant;

public record BlogResponse(
        Long id,
        Long authorId,
        String authorName,
        String authorAvatar,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String description,
        String coverImage,
        String contentType,
        String status,
        String visibility,
        Boolean allowComments,
        Integer words,
        Integer readTime,
        Integer views,
        Integer likeCount,
        Integer commentCount,
        Integer bookmarkCount,
        Integer shareCount,
        Instant publishedAt,
        Instant createdAt,
        Instant updatedAt
) {}
