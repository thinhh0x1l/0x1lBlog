package top.blogapi.gamification.streak.domain.repository;

import top.blogapi.gamification.streak.domain.entity.DailyCheckin;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyCheckinRepository {

    Optional<DailyCheckin> findByUserAndDate(Long userId, LocalDate checkinDate);

    Optional<DailyCheckin> findLastByUserId(Long userId);

    void save(DailyCheckin checkin);

    long countByUserId(Long userId);
}
