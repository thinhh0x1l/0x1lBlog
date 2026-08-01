package top.blogapi.content.blog.interfaces.dto;

import jakarta.validation.constraints.Size;

public record UpdateBlogRequest(
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Size(max = 100000, message = "Content must not exceed 100000 characters")
        String content,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
        String coverImage,

        Long categoryId,

        @Size(max = 50, message = "Content type must not exceed 50 characters")
        String contentType,

        Boolean allowComments
) {}
