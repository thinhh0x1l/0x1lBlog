package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.BlogSeries;
import top.blogapi.service.series.BlogSeriesService;

import java.util.List;

/**
 * Điều phối CRUD loạt bài và quản lý thành viên blog trong loạt.
 */
@Component
@RequiredArgsConstructor
public class SeriesOrchestrator {

    private final BlogSeriesService blogSeriesService;

    public BlogSeries getById(Long id) {
        return blogSeriesService.findById(id);
    }

    public List<BlogSeries> getByAuthor(Long authorId, int page, int size) {
        return blogSeriesService.getByAuthorId(authorId, page, size);
    }

    @Transactional
    public BlogSeries create(BlogSeries series) {
        return blogSeriesService.create(series);
    }

    @Transactional
    public BlogSeries update(Long id, BlogSeries series) {
        series.setId(id);
        return blogSeriesService.update(series);
    }

    @Transactional
    public void delete(Long id) {
        blogSeriesService.softDelete(id);
    }

    @Transactional
    public void addBlog(Long seriesId, Long blogId, int sortOrder) {
        blogSeriesService.addBlog(seriesId, blogId, sortOrder);
    }

    @Transactional
    public void removeBlog(Long seriesId, Long blogId) {
        blogSeriesService.removeBlog(seriesId, blogId);
    }
}
