package top.blogapi.gamification.skill.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.SkillTree;

import java.util.List;

@Repository
public interface SkillTreeJpaRepository extends JpaRepository<SkillTree, Long> {

    List<SkillTree> findByCategoryIdOrderBySortOrder(Long categoryId);

    List<SkillTree> findAllByOrderByCategoryId();
}
