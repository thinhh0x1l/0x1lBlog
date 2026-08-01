package top.blogapi.social.status.domain.repository;

import top.blogapi.social.status.domain.entity.StatusPoll;

import java.util.Optional;

public interface StatusPollRepository {

    Optional<StatusPoll> findById(Long id);

    Optional<StatusPoll> findByStatusId(Long statusId);

    void insert(StatusPoll poll);
}
