package top.blogapi.gamification.badge.domain.repository;

import top.blogapi.gamification.badge.domain.entity.UserBadge;

import java.util.List;

public interface UserBadgeRepository {

    List<UserBadge> findByUserId(Long userId);

    void save(UserBadge userBadge);

    boolean exists(Long userId, Long badgeId);
}
