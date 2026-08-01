package top.blogapi.content.series.domain.repository;

import top.blogapi.content.series.domain.entity.SeriesSubscriber;

import java.util.Optional;

public interface SeriesSubscriberRepository {
    Optional<SeriesSubscriber> findBySeriesAndUser(Long seriesId, Long userId);
    void save(SeriesSubscriber subscriber);
    void delete(Long seriesId, Long userId);
    long countBySeriesId(Long seriesId);
    boolean exists(Long seriesId, Long userId);
}
