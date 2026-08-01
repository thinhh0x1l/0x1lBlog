package top.blogapi.social.status.domain.repository;

import top.blogapi.social.status.domain.entity.Status;

import java.util.List;
import java.util.Optional;

public interface StatusRepository {

    Optional<Status> findById(Long id);

    List<Status> findByUserId(Long userId, int limit, int offset);

    List<Status> findThreadParts(Long threadId);

    List<Status> findFeed(int limit);

    void insert(Status status);

    void update(Status status);

    void softDelete(Long id);

    long countTodayByUserId(Long userId);

    long countByUserId(Long userId);
}
