package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Badge;
import top.blogapi.service.mischief.MischiefService;

import java.util.List;
import java.util.Map;

/**
 * Orchestrates mischief and badge operations: badge listing, awarding, and automated checks.
 */
@Component
@RequiredArgsConstructor
public class MischiefOrchestrator {

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
