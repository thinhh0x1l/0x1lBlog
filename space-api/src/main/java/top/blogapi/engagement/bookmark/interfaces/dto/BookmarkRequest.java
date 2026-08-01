package top.blogapi.engagement.bookmark.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(
        @NotNull(message = "Blog ID is required")
        Long blogId,

        String collection,
        String note,
        Boolean isPublic
) {}
