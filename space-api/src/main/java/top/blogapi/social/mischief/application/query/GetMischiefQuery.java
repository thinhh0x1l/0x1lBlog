package top.blogapi.social.mischief.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.badge.domain.entity.Badge;
import top.blogapi.social.mischief.domain.service.MischiefService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetMischiefQuery {

    private final MischiefService mischiefService;

    public List<Badge> getAllBadges() {
        return mischiefService.getAllBadges();
    }

    @Transactional
    public void awardBadge(Long userId, Long badgeId, Long awardedBy) {
        mischiefService.awardBadge(userId, badgeId, awardedBy);
    }

    public List<Map<String, Object>> getUserBadges(Long userId) {
        return mischiefService.getUserBadges(userId);
    }

    @Transactional
    public List<Badge> checkAndAward(Long userId) {
        return mischiefService.checkAndAward(userId);
    }
}
