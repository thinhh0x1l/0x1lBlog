package top.blogapi.user.auth.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.auth.domain.entity.Session;
import top.blogapi.user.auth.domain.repository.SessionRepository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepository jpaAdapter;
    private final SessionMybatisMapper mybatisAdapter;

    @Override
    public Optional<Session> findById(Long id) {
        return jpaAdapter.findById(id);
    }

    @Override
    public Optional<Session> findBySessionId(String sessionId) {
        return jpaAdapter.findBySessionId(sessionId);
    }

    @Override
    public void insert(Session session) {
        if (session.getStartedAt() == null) {
            session.setStartedAt(Instant.now());
        }
        jpaAdapter.save(session);
    }

    @Override
    public void endSession(Long id) {
        mybatisAdapter.endSession(id);
    }

    @Override
    public void setUserId(Long id, Long userId) {
        mybatisAdapter.setUserId(id, userId);
    }

    @Override
    public void deleteOlderThan90Days() {
        mybatisAdapter.deleteOlderThan90Days();
    }
}
