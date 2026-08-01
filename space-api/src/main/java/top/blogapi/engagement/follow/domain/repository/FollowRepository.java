package top.blogapi.engagement.follow.domain.repository;

import top.blogapi.engagement.follow.domain.entity.Follow;

import java.util.List;
import java.util.Optional;

public interface FollowRepository {

    Optional<Follow> findByPair(Long followerId, Long followingId);

    void insert(Follow follow);

    void delete(Long followerId, Long followingId);

    List<Follow> findByFollowerId(Long followerId, int limit, int offset);

    List<Follow> findByFollowingId(Long followingId, int limit, int offset);

    long countByFollowerId(Long followerId);

    long countByFollowingId(Long followingId);

    boolean exists(Long followerId, Long followingId);
}
