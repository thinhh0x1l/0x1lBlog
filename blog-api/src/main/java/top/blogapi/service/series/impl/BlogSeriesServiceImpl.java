package top.blogapi.service.series.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.BlogSeries;
import top.blogapi.model.entity.SeriesBlog;
import top.blogapi.repository.BlogSeriesRepository;
import top.blogapi.repository.SeriesBlogRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.series.BlogSeriesService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai BlogSeriesService với cache, cung cấp CRUD chuỗi
 * và gán blog vào chuỗi với thứ tự sắp xếp và đồng bộ số lượng bài.
 */
public class BlogSeriesServiceImpl implements BlogSeriesService {

    private final BlogSeriesRepository blogSeriesRepository;
    private final SeriesBlogRepository seriesBlogRepository;
    private final CacheService cacheService;

    @Override
    public BlogSeries create(BlogSeries series) {
        blogSeriesRepository.insert(series);
        return series;
    }

    @Override
    public BlogSeries update(BlogSeries series) {
        blogSeriesRepository.update(series);
        BlogSeries updated = findById(series.getId());
        cacheService.evict(CacheKey.series(updated.getId()));
        return updated;
    }

    @Override
    public BlogSeries findById(Long id) {
        return cacheService.get(
                CacheKey.series(id),
                BlogSeries.class,
                () -> blogSeriesRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.SERIES_NOT_FOUND)),
                CachePolicies.SERIES
        );
    }

    @Override
    public List<BlogSeries> getByAuthorId(Long authorId, int page, int size) {
        return blogSeriesRepository.findByAuthorId(authorId, size, page * size);
    }

    @Override
    public void softDelete(Long id) {
        blogSeriesRepository.softDelete(id);
        cacheService.evict(CacheKey.series(id));
    }

    @Override
    public void addBlog(Long seriesId, Long blogId, int sortOrder) {
        SeriesBlog sb = new SeriesBlog();
        sb.setSeriesId(seriesId);
        sb.setBlogId(blogId);
        sb.setSortOrder(sortOrder);
        seriesBlogRepository.insert(sb);
        blogSeriesRepository.refreshPostCount(seriesId);
        cacheService.evict(CacheKey.series(seriesId));
    }

    @Override
    public void removeBlog(Long seriesId, Long blogId) {
        seriesBlogRepository.delete(seriesId, blogId);
        blogSeriesRepository.refreshPostCount(seriesId);
        cacheService.evict(CacheKey.series(seriesId));
    }
}
