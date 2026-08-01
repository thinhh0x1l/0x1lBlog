package top.blogapi.content.blog.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateBlogRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(max = 100000, message = "Content must not exceed 100000 characters")
        String content,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
        String coverImage,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotBlank(message = "Content type is required")
        @Size(max = 50, message = "Content type must not exceed 50 characters")
        String contentType,

        @Size(max = 200, message = "Location name must not exceed 200 characters")
        String locationName,

        Boolean allowComments,
        List<String> hashtags
) {}
