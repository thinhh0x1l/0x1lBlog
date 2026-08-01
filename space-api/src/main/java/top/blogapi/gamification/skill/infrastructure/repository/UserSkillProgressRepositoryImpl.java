package top.blogapi.gamification.skill.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.UserSkillProgress;
import top.blogapi.gamification.skill.domain.repository.UserSkillProgressRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSkillProgressRepositoryImpl implements UserSkillProgressRepository {

    private final UserSkillProgressJpaRepository jpa;
    private final UserSkillProgressMybatisMapper mybatis;

    @Override
    public Optional<UserSkillProgress> findByUserIdAndCategoryId(Long userId, Long categoryId) {
        return jpa.findByUserIdAndCategoryId(userId, categoryId);
    }

    @Override
    public List<UserSkillProgress> findByUserId(Long userId) {
        return jpa.findByUserId(userId);
    }

    @Override
    public void addPoints(Long userId, Long categoryId, int points) {
        mybatis.addPoints(userId, categoryId, points);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpa.deleteByUserId(userId);
    }
}
