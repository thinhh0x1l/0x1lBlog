package top.blogapi.content.hashtag.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.hashtag.domain.entity.Hashtag;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagJpaRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByName(String name);

    List<Hashtag> findTopByOrderByUsageCountDesc(int limit);

    boolean existsByName(String name);

    @Modifying
    @Query(value = "UPDATE hashtags SET usage_count = usage_count + 1 WHERE id = :id", nativeQuery = true)
    void incrementUsage(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE hashtags SET usage_count = usage_count - 1 WHERE id = :id", nativeQuery = true)
    void decrementUsage(@Param("id") Long id);
}
