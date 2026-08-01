package top.blogapi.gamification.skill.interfaces.dto;

public record SkillTreeDTO(
        Long id,
        Long categoryId,
        String name,
        String description,
        String perkType,
        Integer pointsRequired,
        Integer sortOrder,
        Boolean unlocked
) {}
