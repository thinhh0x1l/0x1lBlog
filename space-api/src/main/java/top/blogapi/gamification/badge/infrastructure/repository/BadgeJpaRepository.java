package top.blogapi.gamification.badge.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.badge.domain.entity.Badge;

import java.util.List;

@Repository
public interface BadgeJpaRepository extends JpaRepository<Badge, Long> {

    @Query(value = "SELECT * FROM badges ORDER BY tier, name", nativeQuery = true)
    List<Badge> findAll();

    boolean existsByName(String name);
}
