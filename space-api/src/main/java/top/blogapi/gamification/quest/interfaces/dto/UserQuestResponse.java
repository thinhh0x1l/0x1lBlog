package top.blogapi.gamification.quest.interfaces.dto;

import java.time.Instant;

public record UserQuestResponse(
        Long id,
        Long questId,
        String questTitle,
        String questType,
        Integer progress,
        Integer target,
        String status,
        Instant claimedAt,
        Instant expiresAt
) {}
