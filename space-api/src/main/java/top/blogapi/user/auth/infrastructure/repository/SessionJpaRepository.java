package top.blogapi.user.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.user.auth.domain.entity.Session;

import java.util.Optional;

@Repository
public interface SessionJpaRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(String sessionId);
}
