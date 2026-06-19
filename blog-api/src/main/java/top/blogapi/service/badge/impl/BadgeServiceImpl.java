package top.blogapi.service.badge.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Badge;
import top.blogapi.model.entity.UserBadge;
import top.blogapi.repository.BadgeRepository;
import top.blogapi.repository.UserBadgeRepository;
import top.blogapi.service.badge.BadgeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Override
    public List<Badge> findAll() {
        return badgeRepository.findAll();
    }

    @Override
    @Transactional
    public Badge create(Badge badge) {
        badgeRepository.insert(badge);
        return badge;
    }

    @Override
    @Transactional
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
