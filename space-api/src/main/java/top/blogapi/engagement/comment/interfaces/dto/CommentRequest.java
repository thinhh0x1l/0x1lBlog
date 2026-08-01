package top.blogapi.engagement.comment.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Target type is required")
        @Size(max = 50, message = "Target type must not exceed 50 characters")
        String targetType,

        @NotNull(message = "Target ID is required")
        Long targetId,

        Long parentId,

        @NotBlank(message = "Content is required")
        @Size(max = 5000, message = "Content must not exceed 5000 characters")
        String content
) {}
