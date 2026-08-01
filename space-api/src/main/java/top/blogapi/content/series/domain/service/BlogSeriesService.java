package top.blogapi.content.series.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.content.series.domain.entity.BlogSeries;
import top.blogapi.content.series.domain.entity.SeriesBlog;
import top.blogapi.content.series.domain.repository.BlogSeriesRepository;
import top.blogapi.content.series.domain.repository.SeriesBlogRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSeriesService {

    private final BlogSeriesRepository blogSeriesRepository;
    private final SeriesBlogRepository seriesBlogRepository;
    private final CacheService cacheService;

    public BlogSeries create(BlogSeries series) {
        blogSeriesRepository.save(series);
        return series;
    }

    public BlogSeries update(BlogSeries series) {
        blogSeriesRepository.save(series);
        BlogSeries updated = findById(series.getId());
        cacheService.evict(CacheKey.series(updated.getId()));
        return updated;
    }

    public BlogSeries findById(Long id) {
        return cacheService.get(
                CacheKey.series(id),
                BlogSeries.class,
                () -> blogSeriesRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.SERIES_NOT_FOUND)),
                CachePolicies.SERIES
        );
    }

    public List<BlogSeries> getByAuthorId(Long authorId, int page, int size) {
        return blogSeriesRepository.findByAuthorId(authorId, size, page * size);
    }

    public void softDelete(Long id) {
        blogSeriesRepository.softDelete(id);
        cacheService.evict(CacheKey.series(id));
    }

    public void addBlog(Long seriesId, Long blogId, int sortOrder) {
        SeriesBlog sb = new SeriesBlog();
        sb.setSeriesId(seriesId);
        sb.setBlogId(blogId);
        sb.setSortOrder(sortOrder);
        seriesBlogRepository.save(sb);
        blogSeriesRepository.refreshPostCount(seriesId);
        cacheService.evict(CacheKey.series(seriesId));
    }

    public void removeBlog(Long seriesId, Long blogId) {
        seriesBlogRepository.delete(seriesId, blogId);
        blogSeriesRepository.refreshPostCount(seriesId);
        cacheService.evict(CacheKey.series(seriesId));
    }
}
