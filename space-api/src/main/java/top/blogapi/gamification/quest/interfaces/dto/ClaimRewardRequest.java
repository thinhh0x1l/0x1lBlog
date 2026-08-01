package top.blogapi.gamification.quest.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record ClaimRewardRequest(
        @NotNull(message = "User quest ID is required")
        Long userQuestId
) {}
