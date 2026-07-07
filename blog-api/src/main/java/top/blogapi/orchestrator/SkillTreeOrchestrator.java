package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.skill.SkillTreeDTO;
import top.blogapi.dto.skill.UnlockRequest;
import top.blogapi.dto.skill.UserSkillDTO;
import top.blogapi.service.skill.SkillTreeService;

import java.util.List;

/**
 * Orchestrates skill tree browsing, point allocation, skill unlocking, and progress tracking.
 */
@Component
@RequiredArgsConstructor
public class SkillTreeOrchestrator {

    private final SkillTreeService skillTreeService;

    public List<SkillTreeDTO> getSkillTrees(Long userId) {
        return skillTreeService.getSkillTrees(userId);
    }

    public List<SkillTreeDTO> getSkillTreesByCategory(Long categoryId, Long userId) {
        return skillTreeService.getSkillTreesByCategory(categoryId, userId);
    }

    @Transactional
    public void addPoints(Long userId, Long categoryId, int points) {
        skillTreeService.addPoints(userId, categoryId, points);
    }

    @Transactional
    public void unlockSkill(Long userId, UnlockRequest request) {
        skillTreeService.unlockSkill(userId, request.getSkillId());
    }

    public UserSkillDTO getUserProgress(Long userId) {
        return skillTreeService.getUserProgress(userId);
    }

    @Transactional
    public void resetSkillTree(Long userId) {
        skillTreeService.resetSkillTree(userId);
    }
}
