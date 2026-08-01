package top.blogapi.gamification.quest.interfaces.dto;

import java.time.Instant;

public record QuestResponse(
        Long id,
        String type,
        String title,
        String description,
        String conditions,
        String rewards,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}
