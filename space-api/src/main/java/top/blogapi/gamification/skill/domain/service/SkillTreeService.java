package top.blogapi.gamification.skill.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.gamification.skill.interfaces.dto.SkillProgressDTO;
import top.blogapi.gamification.skill.interfaces.dto.SkillTreeDTO;
import top.blogapi.gamification.skill.interfaces.dto.UserSkillDTO;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.gamification.skill.domain.entity.SkillTree;
import top.blogapi.gamification.skill.domain.entity.UserSkillUnlock;
import top.blogapi.content.category.domain.repository.CategoryRepository;
import top.blogapi.gamification.skill.domain.repository.SkillTreeRepository;
import top.blogapi.gamification.skill.domain.repository.UserSkillProgressRepository;
import top.blogapi.gamification.skill.domain.repository.UserSkillUnlockRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillTreeService {

    private final SkillTreeRepository skillTreeRepository;
    private final UserSkillProgressRepository userSkillProgressRepository;
    private final UserSkillUnlockRepository userSkillUnlockRepository;
    private final CategoryRepository categoryRepository;

    public List<SkillTreeDTO> getSkillTrees(Long userId) {
        List<SkillTree> skills = skillTreeRepository.findAllByOrderByCategoryIdSortOrder();
        List<Long> unlockedIds = userSkillUnlockRepository.findByUserId(userId).stream()
                .map(UserSkillUnlock::getSkillId)
                .toList();
        return skills.stream()
                .map(s -> toDTO(s, unlockedIds.contains(s.getId())))
                .toList();
    }

    public List<SkillTreeDTO> getSkillTreesByCategory(Long categoryId, Long userId) {
        List<SkillTree> skills = skillTreeRepository.findByCategoryIdOrderBySortOrder(categoryId);
        List<Long> unlockedIds = userSkillUnlockRepository.findByUserId(userId).stream()
                .map(UserSkillUnlock::getSkillId)
                .toList();
        return skills.stream()
                .map(s -> toDTO(s, unlockedIds.contains(s.getId())))
                .toList();
    }

    public void addPoints(Long userId, Long categoryId, int points) {
        userSkillProgressRepository.addPoints(userId, categoryId, points);
    }

    public void unlockSkill(Long userId, Long skillId) {
        if (userSkillUnlockRepository.existsByUserIdAndSkillId(userId, skillId)) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Kỹ năng này đã được mở khóa");
        }

        SkillTree skill = skillTreeRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        var progress = userSkillProgressRepository.findByUserIdAndCategoryId(userId, skill.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_INPUT, "Bạn chưa có điểm trong nhánh kỹ năng này"));

        if (progress.getTotalPoints() < skill.getPointsRequired()) {
            throw new AppException(ErrorCode.INSUFFICIENT_POINT);
        }

        UserSkillUnlock unlock = new UserSkillUnlock();
        unlock.setUserId(userId);
        unlock.setSkillId(skillId);
        userSkillUnlockRepository.save(unlock);
    }

    public UserSkillDTO getUserProgress(Long userId) {
        List<SkillTree> allSkills = skillTreeRepository.findAllByOrderByCategoryIdSortOrder();
        var userProgress = userSkillProgressRepository.findByUserId(userId);
        var userUnlocks = userSkillUnlockRepository.findByUserId(userId);

        List<Long> unlockedIds = userUnlocks.stream()
                .map(UserSkillUnlock::getSkillId)
                .toList();

        List<SkillTreeDTO> skillDTOs = allSkills.stream()
                .map(s -> toDTO(s, unlockedIds.contains(s.getId())))
                .toList();

        List<SkillProgressDTO> progressDTOs = userProgress.stream()
                .map(p -> {
                    Long catId = p.getCategoryId();
                    Integer totalPoints = p.getTotalPoints();
                    String catName = categoryRepository.findById(catId).map(Category::getName).orElse(null);
                    List<Long> unlockedSkillIds = allSkills.stream()
                            .filter(s -> s.getCategoryId().equals(catId) && unlockedIds.contains(s.getId()))
                            .map(SkillTree::getId)
                            .toList();
                    return new SkillProgressDTO(catId, catName, totalPoints, unlockedSkillIds);
                })
                .toList();

        return new UserSkillDTO(progressDTOs, skillDTOs);
    }

    public void resetSkillTree(Long userId) {
        log.info("User {} reset skill tree (would check 200 gems if implemented)", userId);
        userSkillUnlockRepository.deleteByUserId(userId);
        userSkillProgressRepository.deleteByUserId(userId);
    }

    private SkillTreeDTO toDTO(SkillTree s, boolean unlocked) {
        return new SkillTreeDTO(
                s.getId(),
                s.getCategoryId(),
                s.getName(),
                s.getDescription(),
                s.getPerkType(),
                s.getPointsRequired(),
                s.getSortOrder(),
                unlocked
        );
    }
}
