package top.blogapi.gamification.streak.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.streak.domain.service.StreakService;

@Service
@RequiredArgsConstructor
public class CheckinCommand {

    private final StreakService streakService;

    @Transactional
    public int checkin(Long userId) {
        return streakService.checkin(userId);
    }

    public int getStreak(Long userId) {
        return streakService.getStreak(userId);
    }
}
