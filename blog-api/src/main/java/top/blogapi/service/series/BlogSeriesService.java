package top.blogapi.service.series;

import top.blogapi.model.entity.BlogSeries;

import java.util.List;

/**
 * Giao diện service quản lý chuỗi bài viết, cho phép tác giả
 * nhóm các bài blog liên quan thành chuỗi có thứ tự.
 */
public interface BlogSeriesService {
    BlogSeries create(BlogSeries series);
    BlogSeries update(BlogSeries series);
    BlogSeries findById(Long id);
    List<BlogSeries> getByAuthorId(Long authorId, int page, int size);
    void softDelete(Long id);
    void addBlog(Long seriesId, Long blogId, int sortOrder);
    void removeBlog(Long seriesId, Long blogId);
}
