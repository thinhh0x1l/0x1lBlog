package top.blogapi.gamification.skill.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record UnlockRequest(
        @NotNull(message = "Skill ID is required")
        Long skillId
) {}
