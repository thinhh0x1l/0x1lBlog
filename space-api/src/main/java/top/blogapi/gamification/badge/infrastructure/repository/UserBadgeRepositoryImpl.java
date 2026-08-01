package top.blogapi.gamification.badge.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.badge.domain.entity.UserBadge;
import top.blogapi.gamification.badge.domain.repository.UserBadgeRepository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBadgeRepositoryImpl implements UserBadgeRepository {

    private final UserBadgeJpaRepository jpa;

    @Override
    public List<UserBadge> findByUserId(Long userId) {
        return jpa.findByUserId(userId);
    }

    @Override
    public void save(UserBadge userBadge) {
        if (userBadge.getAwardedAt() == null) {
            userBadge.setAwardedAt(Instant.now());
        }
        jpa.save(userBadge);
    }

    @Override
    public boolean exists(Long userId, Long badgeId) {
        return jpa.existsByUserIdAndBadgeId(userId, badgeId);
    }
}
