package top.blogapi.gamification.skill.domain.repository;

import top.blogapi.gamification.skill.domain.entity.UserSkillUnlock;

import java.util.List;
import java.util.Optional;

public interface UserSkillUnlockRepository {

    Optional<UserSkillUnlock> findByUserIdAndSkillId(Long userId, Long skillId);

    List<UserSkillUnlock> findByUserId(Long userId);

    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    void save(UserSkillUnlock unlock);

    void deleteByUserId(Long userId);
}
