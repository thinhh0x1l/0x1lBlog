package top.blogapi.engagement.follow.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.follow.domain.entity.Follow;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Modifying
    @Query(value = "DELETE FROM follows WHERE follower_id = :followerId AND following_id = :followingId", nativeQuery = true)
    void deleteByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId, Pageable pageable);

    List<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId, Pageable pageable);

    long countByFollowerId(Long followerId);

    long countByFollowingId(Long followingId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM follows WHERE follower_id = :followerId AND following_id = :followingId)", nativeQuery = true)
    boolean existsByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}
