package top.blogapi.gamification.skill.interfaces.dto;

import java.util.List;

public record SkillProgressDTO(
        Long categoryId,
        String categoryName,
        Integer totalPoints,
        List<Long> unlockedSkillIds
) {}
