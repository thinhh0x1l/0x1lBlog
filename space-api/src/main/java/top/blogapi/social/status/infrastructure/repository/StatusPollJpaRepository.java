package top.blogapi.social.status.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.StatusPoll;

import java.util.Optional;

@Repository
public interface StatusPollJpaRepository extends JpaRepository<StatusPoll, Long> {

    Optional<StatusPoll> findByStatusId(Long statusId);
}
