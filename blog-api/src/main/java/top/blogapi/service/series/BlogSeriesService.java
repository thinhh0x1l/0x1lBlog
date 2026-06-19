package top.blogapi.service.series;

import top.blogapi.model.entity.BlogSeries;

import java.util.List;

public interface BlogSeriesService {
    BlogSeries create(BlogSeries series);
    BlogSeries update(BlogSeries series);
    BlogSeries findById(Long id);
    List<BlogSeries> getByAuthorId(Long authorId, int page, int size);
    void softDelete(Long id);
    void addBlog(Long seriesId, Long blogId, int sortOrder);
    void removeBlog(Long seriesId, Long blogId);
}
