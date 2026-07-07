package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.service.streak.StreakService;

/**
 * Điều phối điểm danh hàng ngày và theo dõi chuỗi tương tác người dùng.
 */
@Component
@RequiredArgsConstructor
public class StreakOrchestrator {

    private final StreakService streakService;

    @Transactional
    public int checkin(Long userId) {
        return streakService.checkin(userId);
    }

    public int getStreak(Long userId) {
        return streakService.getStreak(userId);
    }
}
