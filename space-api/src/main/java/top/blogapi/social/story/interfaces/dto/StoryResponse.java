package top.blogapi.social.story.interfaces.dto;

import java.time.Instant;

public record StoryResponse(
        Long id,
        Long userId,
        String mediaUrl,
        String mediaType,
        String textContent,
        String visibility,
        Long viewCount,
        Instant expiresAt,
        Instant createdAt
) {}
