package top.blogapi.engagement.follow.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(
        @NotNull(message = "Following ID is required")
        Long followingId
) {}
