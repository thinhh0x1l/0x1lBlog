package top.blogapi.service.skill;

import top.blogapi.dto.skill.SkillTreeDTO;
import top.blogapi.dto.skill.SkillProgressDTO;
import top.blogapi.dto.skill.UserSkillDTO;

import java.util.List;

/**
 * Giao diện service cho hệ thống cây kỹ năng, quản lý mở khóa kỹ năng,
 * phân bổ điểm theo danh mục và đặt lại cây kỹ năng.
 */
public interface SkillTreeService {
    List<SkillTreeDTO> getSkillTrees(Long userId);
    List<SkillTreeDTO> getSkillTreesByCategory(Long categoryId, Long userId);
    void addPoints(Long userId, Long categoryId, int points);
    void unlockSkill(Long userId, Long skillId);
    UserSkillDTO getUserProgress(Long userId);
    void resetSkillTree(Long userId);
}
