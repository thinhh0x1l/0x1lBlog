package top.blogapi.service.streak.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.DailyCheckin;
import top.blogapi.model.entity.User;
import top.blogapi.model.entity.UserExpLog;
import top.blogapi.repository.DailyCheckinRepository;
import top.blogapi.repository.UserExpLogRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.streak.StreakService;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai StreakService quản lý điểm danh hàng ngày với theo dõi chuỗi,
 * chống điểm danh trùng trong ngày và thưởng kinh nghiệm theo cấp.
 */
public class StreakServiceImpl implements StreakService {

    private final UserRepository userRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final UserExpLogRepository userExpLogRepository;

    @Override
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
        dailyCheckinRepository.insert(checkin);

        userRepository.updateCheckin(userId, newStreak, today);

        if (bonusExp > 0) {
            UserExpLog expLog = new UserExpLog();
            expLog.setUserId(userId);
            expLog.setAmount(bonusExp);
            expLog.setReason("CHECKIN_BONUS");
            userExpLogRepository.insert(expLog);
        }

        return newStreak;
    }

    @Override
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
