package top.blogapi.gamification.skill.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.UserSkillUnlock;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillUnlockJpaRepository extends JpaRepository<UserSkillUnlock, Long> {

    Optional<UserSkillUnlock> findByUserIdAndSkillId(Long userId, Long skillId);

    List<UserSkillUnlock> findByUserId(Long userId);

    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    @Modifying
    @Query(value = "DELETE FROM user_skill_unlocks WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);
}
