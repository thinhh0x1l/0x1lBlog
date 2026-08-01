package top.blogapi.content.blog.domain.repository;

import top.blogapi.content.blog.domain.entity.Blog;

import java.util.List;
import java.util.Optional;

public interface BlogRepository {
    Optional<Blog> findById(Long id);
    Optional<Blog> findBySlug(String slug);
    List<Blog> findPublished(int limit, int offset);
    List<Blog> findByAuthorId(Long authorId, int limit, int offset);
    List<Blog> findByCategoryId(Long categoryId, int limit, int offset);
    void save(Blog blog);
    void updateStatus(Long id, String status);
    void softDelete(Long id);
    long countPublished();
    long countByAuthorId(Long authorId);
    List<Blog> findTrending(int limit);
    List<Blog> findRecommended(int limit);
    void toggleTop(Long id, boolean isTop);
    void toggleRecommend(Long id, boolean isRecommend);
    void incrementViews(Long id);
    void incrementCommentCount(Long id);
    void incrementBookmarkCount(Long id);
    void decrementBookmarkCount(Long id);
    void incrementViewsBy(Long id, long count);
    List<Blog> search(String keyword, int limit, int offset);
    long countSearch(String keyword);
    Optional<Blog> findRandomPublished();
    Optional<Blog> findRandomByCategoryId(Long categoryId);
}
