package top.blogapi.gamification.blind.interfaces.dto;

public record BlindGuessDTO(
        Long id,
        Long challengeId,
        Long guessedTopicId,
        Boolean isCorrect
) {}
