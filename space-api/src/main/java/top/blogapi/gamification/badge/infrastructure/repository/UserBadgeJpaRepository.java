package top.blogapi.gamification.badge.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.badge.domain.entity.UserBadge;

import java.util.List;

@Repository
public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {

    @Query(value = "SELECT * FROM user_badges WHERE user_id = :userId ORDER BY awarded_at DESC", nativeQuery = true)
    List<UserBadge> findByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);
}
