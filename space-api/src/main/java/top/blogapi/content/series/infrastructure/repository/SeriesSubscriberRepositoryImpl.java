package top.blogapi.content.series.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.SeriesSubscriber;
import top.blogapi.content.series.domain.repository.SeriesSubscriberRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeriesSubscriberRepositoryImpl implements SeriesSubscriberRepository {

    private final SeriesSubscriberJpaRepository jpa;

    @Override
    public Optional<SeriesSubscriber> findBySeriesAndUser(Long seriesId, Long userId) {
        return jpa.findBySeriesIdAndUserId(seriesId, userId);
    }

    @Override
    public void save(SeriesSubscriber subscriber) {
        jpa.save(subscriber);
    }

    @Override
    public void delete(Long seriesId, Long userId) {
        jpa.deleteBySeriesIdAndUserId(seriesId, userId);
    }

    @Override
    public long countBySeriesId(Long seriesId) {
        return jpa.countBySeriesId(seriesId);
    }

    @Override
    public boolean exists(Long seriesId, Long userId) {
        return jpa.existsBySeriesIdAndUserId(seriesId, userId);
    }
}
