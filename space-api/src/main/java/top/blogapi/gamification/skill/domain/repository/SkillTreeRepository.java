package top.blogapi.gamification.skill.domain.repository;

import top.blogapi.gamification.skill.domain.entity.SkillTree;

import java.util.List;
import java.util.Optional;

public interface SkillTreeRepository {

    List<SkillTree> findByCategoryIdOrderBySortOrder(Long categoryId);

    List<SkillTree> findAllByOrderByCategoryIdSortOrder();

    Optional<SkillTree> findById(Long id);
}
