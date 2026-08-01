package top.blogapi.gamification.skill.domain.repository;

import top.blogapi.gamification.skill.domain.entity.UserSkillProgress;

import java.util.List;
import java.util.Optional;

public interface UserSkillProgressRepository {

    Optional<UserSkillProgress> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<UserSkillProgress> findByUserId(Long userId);

    void addPoints(Long userId, Long categoryId, int points);

    void deleteByUserId(Long userId);
}
