package top.blogapi.content.series.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.SeriesSubscriber;

import java.util.Optional;

@Repository
public interface SeriesSubscriberJpaRepository extends JpaRepository<SeriesSubscriber, Long> {

    Optional<SeriesSubscriber> findBySeriesIdAndUserId(Long seriesId, Long userId);

    @Modifying
    @Query(value = "DELETE FROM series_subscribers WHERE series_id = :seriesId AND user_id = :userId", nativeQuery = true)
    void deleteBySeriesIdAndUserId(@Param("seriesId") Long seriesId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM series_subscribers WHERE series_id = :seriesId", nativeQuery = true)
    long countBySeriesId(@Param("seriesId") Long seriesId);

    boolean existsBySeriesIdAndUserId(Long seriesId, Long userId);
}
