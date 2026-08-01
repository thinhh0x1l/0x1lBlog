package top.blogapi.gamification.reputation.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.reputation.domain.entity.UserExpLog;
import top.blogapi.gamification.reputation.domain.repository.UserExpLogRepository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserExpLogRepositoryImpl implements UserExpLogRepository {

    private final UserExpLogJpaRepository jpa;

    @Override
    public List<UserExpLog> findByUserId(Long userId, int limit, int offset) {
        return jpa.findByUserId(userId, limit, offset);
    }

    @Override
    public void save(UserExpLog log) {
        if (log.getCreatedAt() == null) {
            log.setCreatedAt(Instant.now());
        }
        jpa.save(log);
    }

    @Override
    public long sumExpByUserId(Long userId) {
        return jpa.sumExpByUserId(userId);
    }
}
