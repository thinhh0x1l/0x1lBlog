package top.blogapi.service.skill.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.BadRequestException;
import top.blogapi.common.exception.ResourceNotFoundException;
import top.blogapi.dto.skill.SkillProgressDTO;
import top.blogapi.dto.skill.SkillTreeDTO;
import top.blogapi.dto.skill.UserSkillDTO;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.skill.SkillTree;
import top.blogapi.model.entity.skill.UserSkillUnlock;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.repository.skill.SkillTreeRepository;
import top.blogapi.repository.skill.UserSkillProgressRepository;
import top.blogapi.repository.skill.UserSkillUnlockRepository;
import top.blogapi.service.skill.SkillTreeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai SkillTreeService quản lý tiến triển cây kỹ năng, phân bổ điểm
 * theo danh mục, xác thực mở khóa và truy vấn tiến độ toàn bộ người dùng.
 */
public class SkillTreeServiceImpl implements SkillTreeService {

    private final SkillTreeRepository skillTreeRepository;
    private final UserSkillProgressRepository userSkillProgressRepository;
    private final UserSkillUnlockRepository userSkillUnlockRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<SkillTreeDTO> getSkillTrees(Long userId) {
        List<SkillTree> skills = skillTreeRepository.findAllByOrderByCategoryIdSortOrder();
        List<Long> unlockedIds = userSkillUnlockRepository.findByUserId(userId).stream()
                .map(UserSkillUnlock::getSkillId)
                .toList();
        return skills.stream()
                .map(s -> toDTO(s, unlockedIds.contains(s.getId())))
                .toList();
    }

    @Override
    public List<SkillTreeDTO> getSkillTreesByCategory(Long categoryId, Long userId) {
        List<SkillTree> skills = skillTreeRepository.findByCategoryIdOrderBySortOrder(categoryId);
        List<Long> unlockedIds = userSkillUnlockRepository.findByUserId(userId).stream()
                .map(UserSkillUnlock::getSkillId)
                .toList();
        return skills.stream()
                .map(s -> toDTO(s, unlockedIds.contains(s.getId())))
                .toList();
    }

    @Override
    public void addPoints(Long userId, Long categoryId, int points) {
        userSkillProgressRepository.addPoints(userId, categoryId, points);
    }

    @Override
    public void unlockSkill(Long userId, Long skillId) {
        if (userSkillUnlockRepository.existsByUserIdAndSkillId(userId, skillId)) {
            throw new BadRequestException("Kỹ năng này đã được mở khóa");
        }

        SkillTree skill = skillTreeRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("SkillTree", "id", skillId));

        var progress = userSkillProgressRepository.findByUserIdAndCategoryId(userId, skill.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Bạn chưa có điểm trong nhánh kỹ năng này"));

        if (progress.getTotalPoints() < skill.getPointsRequired()) {
            throw new BadRequestException("Không đủ điểm để mở khóa kỹ năng này");
        }

        UserSkillUnlock unlock = new UserSkillUnlock();
        unlock.setUserId(userId);
        unlock.setSkillId(skillId);
        userSkillUnlockRepository.insert(unlock);
    }

    @Override
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
                    SkillProgressDTO dto = new SkillProgressDTO();
                    dto.setCategoryId(p.getCategoryId());
                    dto.setTotalPoints(p.getTotalPoints());
                    categoryRepository.findById(p.getCategoryId())
                            .ifPresent(c -> dto.setCategoryName(c.getName()));
                    dto.setUnlockedSkillIds(allSkills.stream()
                            .filter(s -> s.getCategoryId().equals(p.getCategoryId()) && unlockedIds.contains(s.getId()))
                            .map(SkillTree::getId)
                            .toList());
                    return dto;
                })
                .toList();

        UserSkillDTO result = new UserSkillDTO();
        result.setProgress(progressDTOs);
        result.setSkillTrees(skillDTOs);
        return result;
    }

    @Override
    public void resetSkillTree(Long userId) {
        log.info("User {} reset skill tree (would check 200 gems if implemented)", userId);
        userSkillUnlockRepository.deleteByUserId(userId);
        userSkillProgressRepository.deleteByUserId(userId);
    }

    private SkillTreeDTO toDTO(SkillTree s, boolean unlocked) {
        SkillTreeDTO dto = new SkillTreeDTO();
        dto.setId(s.getId());
        dto.setCategoryId(s.getCategoryId());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        dto.setPerkType(s.getPerkType());
        dto.setPointsRequired(s.getPointsRequired());
        dto.setSortOrder(s.getSortOrder());
        dto.setUnlocked(unlocked);
        return dto;
    }
}
