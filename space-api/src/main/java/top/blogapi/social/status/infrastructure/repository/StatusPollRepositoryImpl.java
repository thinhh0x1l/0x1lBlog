package top.blogapi.social.status.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.StatusPoll;
import top.blogapi.social.status.domain.repository.StatusPollRepository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StatusPollRepositoryImpl implements StatusPollRepository {

    private final StatusPollJpaRepository jpa;

    @Override
    public Optional<StatusPoll> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<StatusPoll> findByStatusId(Long statusId) {
        return jpa.findByStatusId(statusId);
    }

    @Override
    public void insert(StatusPoll poll) {
        if (poll.getCreatedAt() == null) {
            poll.setCreatedAt(Instant.now());
        }
        jpa.save(poll);
    }
}
