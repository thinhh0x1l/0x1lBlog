package top.blogapi.gamification.badge.domain.repository;

import top.blogapi.gamification.badge.domain.entity.Badge;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository {

    Optional<Badge> findById(Long id);

    List<Badge> findAll();

    void save(Badge badge);

    boolean existsByName(String name);
}
