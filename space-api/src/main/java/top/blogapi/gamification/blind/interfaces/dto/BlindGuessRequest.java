package top.blogapi.gamification.blind.interfaces.dto;

import jakarta.validation.constraints.NotNull;

public record BlindGuessRequest(
        @NotNull(message = "Guessed topic ID is required")
        Long guessedTopicId
) {}
