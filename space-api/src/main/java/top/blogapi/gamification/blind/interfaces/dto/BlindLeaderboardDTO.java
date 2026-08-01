package top.blogapi.gamification.blind.interfaces.dto;

import java.time.Instant;

public record BlindLeaderboardDTO(
        Long userId,
        Instant guessedAt
) {}
