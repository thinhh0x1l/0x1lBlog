package top.blogapi.gamification.streak.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.streak.domain.entity.DailyCheckin;

import java.util.Optional;

@Repository
public interface DailyCheckinJpaRepository extends JpaRepository<DailyCheckin, Long> {

    @Query(value = "SELECT * FROM daily_checkins WHERE user_id = :userId AND checkin_date = :checkinDate", nativeQuery = true)
    Optional<DailyCheckin> findByUserAndDate(@Param("userId") Long userId, @Param("checkinDate") String checkinDate);

    @Query(value = "SELECT * FROM daily_checkins WHERE user_id = :userId ORDER BY checkin_date DESC LIMIT 1", nativeQuery = true)
    Optional<DailyCheckin> findLastByUserId(@Param("userId") Long userId);

    long countByUserId(Long userId);
}
