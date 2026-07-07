package top.blogapi.service.badge.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.Badge;
import top.blogapi.model.entity.UserBadge;
import top.blogapi.repository.BadgeRepository;
import top.blogapi.repository.UserBadgeRepository;
import top.blogapi.service.badge.BadgeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai BadgeService cung cấp CRUD huy hiệu và trao thưởng
 * với cơ chế chống trùng lặp.
 */
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Override
    public List<Badge> findAll() {
        return badgeRepository.findAll();
    }

    @Override
    public Badge create(Badge badge) {
        badgeRepository.insert(badge);
        return badge;
    }

    @Override
    public void awardBadge(Long userId, Long badgeId, Long awardedBy) {
        if (!hasBadge(userId, badgeId)) {
            UserBadge ub = new UserBadge();
            ub.setUserId(userId);
            ub.setBadgeId(badgeId);
            ub.setAwardedBy(awardedBy);
            userBadgeRepository.insert(ub);
        }
    }

    @Override
    public boolean hasBadge(Long userId, Long badgeId) {
        return userBadgeRepository.exists(userId, badgeId);
    }
}
