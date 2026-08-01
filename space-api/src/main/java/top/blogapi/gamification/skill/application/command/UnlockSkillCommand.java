package top.blogapi.gamification.skill.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.skill.interfaces.dto.UnlockRequest;
import top.blogapi.gamification.skill.domain.service.SkillTreeService;

@Service
@RequiredArgsConstructor
public class UnlockSkillCommand {

    private final SkillTreeService skillTreeService;

    @Transactional
    public void addPoints(Long userId, Long categoryId, int points) {
        skillTreeService.addPoints(userId, categoryId, points);
    }

    @Transactional
    public void unlockSkill(Long userId, UnlockRequest request) {
        skillTreeService.unlockSkill(userId, request.skillId());
    }

    @Transactional
    public void resetSkillTree(Long userId) {
        skillTreeService.resetSkillTree(userId);
    }
}
