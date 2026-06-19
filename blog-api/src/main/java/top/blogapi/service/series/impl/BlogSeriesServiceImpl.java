package top.blogapi.service.series.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.BlogSeries;
import top.blogapi.model.entity.SeriesBlog;
import top.blogapi.repository.BlogSeriesRepository;
import top.blogapi.repository.SeriesBlogRepository;
import top.blogapi.service.series.BlogSeriesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogSeriesServiceImpl implements BlogSeriesService {

    private final BlogSeriesRepository blogSeriesRepository;
    private final SeriesBlogRepository seriesBlogRepository;

    @Override
    @Transactional
    @CacheEvict(value = "series", allEntries = true)
    public BlogSeries create(BlogSeries series) {
        blogSeriesRepository.insert(series);
        return series;
    }

    @Override
    @Transactional
    @CacheEvict(value = "series", key = "'findById:' + #series.id")
    public BlogSeries update(BlogSeries series) {
        blogSeriesRepository.update(series);
        return findById(series.getId());
    }

    @Override
    @Cacheable(value = "series", key = "'findById:' + #id")
    public BlogSeries findById(Long id) {
        return blogSeriesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERIES_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "series", key = "'getByAuthorId:' + #authorId + ':' + #page + ':' + #size")
    public List<BlogSeries> getByAuthorId(Long authorId, int page, int size) {
        return blogSeriesRepository.findByAuthorId(authorId, size, page * size);
    }

    @Override
    @Transactional
    @CacheEvict(value = "series", allEntries = true)
    public void softDelete(Long id) {
        blogSeriesRepository.softDelete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "series", allEntries = true)
    public void addBlog(Long seriesId, Long blogId, int sortOrder) {
        SeriesBlog sb = new SeriesBlog();
        sb.setSeriesId(seriesId);
        sb.setBlogId(blogId);
        sb.setSortOrder(sortOrder);
        seriesBlogRepository.insert(sb);
        blogSeriesRepository.refreshPostCount(seriesId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "series", allEntries = true)
    public void removeBlog(Long seriesId, Long blogId) {
        seriesBlogRepository.delete(seriesId, blogId);
        blogSeriesRepository.refreshPostCount(seriesId);
    }
}
