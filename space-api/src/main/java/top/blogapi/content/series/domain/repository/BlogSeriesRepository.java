package top.blogapi.content.series.domain.repository;

import top.blogapi.content.series.domain.entity.BlogSeries;

import java.util.List;
import java.util.Optional;

public interface BlogSeriesRepository {
    Optional<BlogSeries> findById(Long id);
    List<BlogSeries> findByAuthorId(Long authorId, int limit, int offset);
    void save(BlogSeries series);
    void softDelete(Long id);
    void refreshPostCount(Long seriesId);
    void refreshSubscriberCount(Long seriesId);
}
