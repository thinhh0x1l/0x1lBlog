package top.blogapi.engagement.follow.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.follow.domain.entity.Follow;
import top.blogapi.engagement.follow.domain.repository.FollowRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

    private final FollowJpaRepository jpa;

    @Override
    public Optional<Follow> findByPair(Long followerId, Long followingId) {
        return jpa.findByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public void insert(Follow follow) {
        jpa.save(follow);
    }

    @Override
    public void delete(Long followerId, Long followingId) {
        jpa.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public List<Follow> findByFollowerId(Long followerId, int limit, int offset) {
        return jpa.findByFollowerIdOrderByCreatedAtDesc(followerId, PageRequest.of(offset / limit, limit));
    }

    @Override
    public List<Follow> findByFollowingId(Long followingId, int limit, int offset) {
        return jpa.findByFollowingIdOrderByCreatedAtDesc(followingId, PageRequest.of(offset / limit, limit));
    }

    @Override
    public long countByFollowerId(Long followerId) {
        return jpa.countByFollowerId(followerId);
    }

    @Override
    public long countByFollowingId(Long followingId) {
        return jpa.countByFollowingId(followingId);
    }

    @Override
    public boolean exists(Long followerId, Long followingId) {
        return jpa.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
