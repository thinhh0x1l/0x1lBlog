package top.blogapi.gamification.skill.interfaces.dto;

import java.util.List;

public record UserSkillDTO(
        List<SkillProgressDTO> progress,
        List<SkillTreeDTO> skillTrees
) {}
