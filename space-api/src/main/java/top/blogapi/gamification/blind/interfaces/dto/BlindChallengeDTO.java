package top.blogapi.gamification.blind.interfaces.dto;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record BlindChallengeDTO(
        Long id,
        LocalDate date,
        String topicHint,
        List<Map<String, Object>> options,
        Boolean revealed,
        Instant createdAt
) {}
