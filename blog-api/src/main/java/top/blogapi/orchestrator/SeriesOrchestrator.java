package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.BlogSeries;
import top.blogapi.service.series.BlogSeriesService;

import java.util.List;

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

    public BlogSeries create(BlogSeries series) {
        return blogSeriesService.create(series);
    }

    public BlogSeries update(Long id, BlogSeries series) {
        series.setId(id);
        return blogSeriesService.update(series);
    }

    public void delete(Long id) {
        blogSeriesService.softDelete(id);
    }

    public void addBlog(Long seriesId, Long blogId, int sortOrder) {
        blogSeriesService.addBlog(seriesId, blogId, sortOrder);
    }

    public void removeBlog(Long seriesId, Long blogId) {
        blogSeriesService.removeBlog(seriesId, blogId);
    }
}
