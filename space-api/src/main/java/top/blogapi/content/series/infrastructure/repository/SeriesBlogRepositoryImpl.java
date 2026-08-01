package top.blogapi.content.series.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.SeriesBlog;
import top.blogapi.content.series.domain.repository.SeriesBlogRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeriesBlogRepositoryImpl implements SeriesBlogRepository {

    private final SeriesBlogJpaRepository jpa;

    @Override
    public List<SeriesBlog> findBySeriesId(Long seriesId) {
        return jpa.findBySeriesIdOrderBySortOrder(seriesId);
    }

    @Override
    public void save(SeriesBlog seriesBlog) {
        jpa.save(seriesBlog);
    }

    @Override
    public void delete(Long seriesId, Long blogId) {
        jpa.deleteBySeriesIdAndBlogId(seriesId, blogId);
    }

    @Override
    public void deleteBySeriesId(Long seriesId) {
        jpa.deleteBySeriesId(seriesId);
    }
}
