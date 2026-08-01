package top.blogapi.gamification.streak.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.gamification.streak.domain.entity.DailyCheckin;
import top.blogapi.user.core.entity.User;
import top.blogapi.gamification.reputation.domain.entity.UserExpLog;
import top.blogapi.gamification.streak.domain.repository.DailyCheckinRepository;
import top.blogapi.gamification.reputation.domain.repository.UserExpLogRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreakService {

    private final UserRepository userRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final UserExpLogRepository userExpLogRepository;

    public int checkin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();

        if (user.getLastCheckinAt() != null && user.getLastCheckinAt().equals(today)) {
            throw new AppException(ErrorCode.ALREADY_CHECKED_IN);
        }

        int newStreak;
        if (user.getLastCheckinAt() != null && user.getLastCheckinAt().equals(today.minusDays(1))) {
            newStreak = (user.getCheckinStreak() != null ? user.getCheckinStreak() : 0) + 1;
        } else {
            newStreak = 1;
        }

        int bonusExp = calculateBonusExp(newStreak);

        DailyCheckin checkin = new DailyCheckin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(today.toString());
        checkin.setStreakAtTime(newStreak);
        checkin.setBonusExp(bonusExp);
        dailyCheckinRepository.save(checkin);

        userRepository.updateCheckin(userId, newStreak, today);

        if (bonusExp > 0) {
            UserExpLog expLog = new UserExpLog();
            expLog.setUserId(userId);
            expLog.setAmount(bonusExp);
            expLog.setReason("CHECKIN_BONUS");
            userExpLogRepository.save(expLog);
        }

        return newStreak;
    }

    public int getStreak(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getCheckinStreak() != null ? user.getCheckinStreak() : 0;
    }

    private int calculateBonusExp(int streak) {
        if (streak >= 30) return 100;
        if (streak >= 14) return 50;
        if (streak >= 7) return 30;
        if (streak >= 3) return 15;
        return 5;
    }
}
