package top.blogapi.service.blog;

import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.Hashtag;

import java.util.List;

public interface BlogService {
    Blog create(Blog blog);
    Blog update(Blog blog);
    Blog findById(Long id);
    Blog findBySlug(String slug);
    void softDelete(Long id);

    void publish(Long id);
    void archive(Long id);

    List<Blog> getPublished(int page, int size);
    List<Blog> getByAuthorId(Long authorId, int page, int size);
    List<Blog> getByCategoryId(Long categoryId, int page, int size);
    List<Blog> getTrending(int limit);
    List<Blog> getRecommended(int limit);
    List<Blog> search(String keyword, int page, int size);

    long countPublished();
    long countByAuthorId(Long authorId);

    void incrementViews(Long id);
    void updateCategoryCounters(Long categoryId);
}
