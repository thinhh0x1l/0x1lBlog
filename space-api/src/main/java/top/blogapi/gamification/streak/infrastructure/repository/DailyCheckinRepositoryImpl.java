package top.blogapi.gamification.streak.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.streak.domain.entity.DailyCheckin;
import top.blogapi.gamification.streak.domain.repository.DailyCheckinRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyCheckinRepositoryImpl implements DailyCheckinRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final DailyCheckinJpaRepository jpa;

    @Override
    public Optional<DailyCheckin> findByUserAndDate(Long userId, LocalDate checkinDate) {
        return jpa.findByUserAndDate(userId, checkinDate.format(FORMATTER));
    }

    @Override
    public Optional<DailyCheckin> findLastByUserId(Long userId) {
        return jpa.findLastByUserId(userId);
    }

    @Override
    public void save(DailyCheckin checkin) {
        if (checkin.getCreatedAt() == null) {
            checkin.setCreatedAt(Instant.now());
        }
        jpa.save(checkin);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpa.countByUserId(userId);
    }
}
