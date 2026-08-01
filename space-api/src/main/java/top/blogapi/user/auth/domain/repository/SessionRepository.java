package top.blogapi.user.auth.domain.repository;

import top.blogapi.user.auth.domain.entity.Session;

import java.util.Optional;

public interface SessionRepository {

    Optional<Session> findById(Long id);

    Optional<Session> findBySessionId(String sessionId);

    void insert(Session session);

    void endSession(Long id);

    void setUserId(Long id, Long userId);

    void deleteOlderThan90Days();
}
