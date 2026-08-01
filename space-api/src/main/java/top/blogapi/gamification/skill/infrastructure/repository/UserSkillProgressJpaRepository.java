package top.blogapi.gamification.skill.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.UserSkillProgress;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillProgressJpaRepository extends JpaRepository<UserSkillProgress, Long> {

    Optional<UserSkillProgress> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<UserSkillProgress> findByUserId(Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_skill_progress WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);
}
