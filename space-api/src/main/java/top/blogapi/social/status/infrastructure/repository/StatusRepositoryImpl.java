package top.blogapi.social.status.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.Status;
import top.blogapi.social.status.domain.repository.StatusRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StatusRepositoryImpl implements StatusRepository {

    private final StatusJpaRepository jpa;
    private final StatusMybatisMapper mybatis;

    @Override
    public Optional<Status> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Status> findByUserId(Long userId, int limit, int offset) {
        return mybatis.findByUserId(userId, limit, offset);
    }

    @Override
    public List<Status> findThreadParts(Long threadId) {
        return mybatis.findThreadParts(threadId);
    }

    @Override
    public List<Status> findFeed(int limit) {
        return mybatis.findFeed(limit);
    }

    @Override
    public void insert(Status status) {
        jpa.save(status);
    }

    @Override
    public void update(Status status) {
        mybatis.update(status);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public long countTodayByUserId(Long userId) {
        return jpa.countTodayByUserId(userId);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpa.countByUserId(userId);
    }
}
