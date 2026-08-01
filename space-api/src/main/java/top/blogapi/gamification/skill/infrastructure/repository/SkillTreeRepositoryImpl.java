package top.blogapi.gamification.skill.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.SkillTree;
import top.blogapi.gamification.skill.domain.repository.SkillTreeRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SkillTreeRepositoryImpl implements SkillTreeRepository {

    private final SkillTreeJpaRepository jpa;

    @Override
    public List<SkillTree> findByCategoryIdOrderBySortOrder(Long categoryId) {
        return jpa.findByCategoryIdOrderBySortOrder(categoryId);
    }

    @Override
    public List<SkillTree> findAllByOrderByCategoryIdSortOrder() {
        return jpa.findAllByOrderByCategoryId();
    }

    @Override
    public Optional<SkillTree> findById(Long id) {
        return jpa.findById(id);
    }
}
