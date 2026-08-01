package top.blogapi.content.series.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.BlogSeries;
import top.blogapi.content.series.domain.repository.BlogSeriesRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogSeriesRepositoryImpl implements BlogSeriesRepository {

    private final BlogSeriesJpaRepository jpa;
    private final BlogSeriesMybatisMapper mybatis;

    @Override
    public Optional<BlogSeries> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<BlogSeries> findByAuthorId(Long authorId, int limit, int offset) {
        List<BlogSeries> all = jpa.findByAuthorIdOrderByCreatedAtDesc(authorId);
        int start = Math.min(offset, all.size());
        int end = Math.min(start + limit, all.size());
        return all.subList(start, end);
    }

    @Override
    public void save(BlogSeries series) {
        jpa.save(series);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public void refreshPostCount(Long seriesId) {
        mybatis.refreshPostCount(seriesId);
    }

    @Override
    public void refreshSubscriberCount(Long seriesId) {
        mybatis.refreshSubscriberCount(seriesId);
    }
}
