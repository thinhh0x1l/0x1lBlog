package top.blogapi.gamification.skill.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.gamification.skill.interfaces.dto.SkillTreeDTO;
import top.blogapi.gamification.skill.interfaces.dto.UserSkillDTO;
import top.blogapi.gamification.skill.domain.service.SkillTreeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSkillTreeQuery {

    private final SkillTreeService skillTreeService;

    public List<SkillTreeDTO> getSkillTrees(Long userId) {
        return skillTreeService.getSkillTrees(userId);
    }

    public List<SkillTreeDTO> getSkillTreesByCategory(Long categoryId, Long userId) {
        return skillTreeService.getSkillTreesByCategory(categoryId, userId);
    }

    public UserSkillDTO getUserProgress(Long userId) {
        return skillTreeService.getUserProgress(userId);
    }
}
