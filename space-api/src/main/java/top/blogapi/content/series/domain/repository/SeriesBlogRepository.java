package top.blogapi.content.series.domain.repository;

import top.blogapi.content.series.domain.entity.SeriesBlog;

import java.util.List;

public interface SeriesBlogRepository {
    List<SeriesBlog> findBySeriesId(Long seriesId);
    void save(SeriesBlog seriesBlog);
    void delete(Long seriesId, Long blogId);
    void deleteBySeriesId(Long seriesId);
}
