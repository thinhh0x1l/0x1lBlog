package top.blogapi.gamification.badge.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.gamification.badge.domain.entity.Badge;
import top.blogapi.gamification.badge.domain.entity.UserBadge;
import top.blogapi.gamification.badge.domain.repository.BadgeRepository;
import top.blogapi.gamification.badge.domain.repository.UserBadgeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public List<Badge> findAll() {
        return badgeRepository.findAll();
    }

    public Badge create(Badge badge) {
        badgeRepository.save(badge);
        return badge;
    }

    public void awardBadge(Long userId, Long badgeId, Long awardedBy) {
        if (!hasBadge(userId, badgeId)) {
            UserBadge ub = new UserBadge();
            ub.setUserId(userId);
            ub.setBadgeId(badgeId);
            ub.setAwardedBy(awardedBy);
            userBadgeRepository.save(ub);
        }
    }

    public boolean hasBadge(Long userId, Long badgeId) {
        return userBadgeRepository.exists(userId, badgeId);
    }
}
