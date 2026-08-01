package top.blogapi.content.blog.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.repository.BlogRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogRepositoryImpl implements BlogRepository {

    private final BlogJpaRepository jpa;
    private final BlogMybatisMapper mybatis;

    @Override
    public Optional<Blog> findById(Long id) {
        return mybatis.findById(id);
    }

    @Override
    public Optional<Blog> findBySlug(String slug) {
        return mybatis.findBySlug(slug);
    }

    @Override
    public List<Blog> findPublished(int limit, int offset) {
        return mybatis.findPublished(limit, offset);
    }

    @Override
    public List<Blog> findByAuthorId(Long authorId, int limit, int offset) {
        return mybatis.findByAuthorId(authorId, limit, offset);
    }

    @Override
    public List<Blog> findByCategoryId(Long categoryId, int limit, int offset) {
        return mybatis.findByCategoryId(categoryId, limit, offset);
    }

    @Override
    public void save(Blog blog) {
        jpa.save(blog);
    }

    @Override
    public void updateStatus(Long id, String status) {
        jpa.updateStatus(id, status);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public long countPublished() {
        return jpa.countPublished();
    }

    @Override
    public long countByAuthorId(Long authorId) {
        return jpa.countByAuthorId(authorId);
    }

    @Override
    public List<Blog> findTrending(int limit) {
        return mybatis.findTrending(limit);
    }

    @Override
    public List<Blog> findRecommended(int limit) {
        return mybatis.findRecommended(limit);
    }

    @Override
    public void toggleTop(Long id, boolean isTop) {
        jpa.toggleTop(id, isTop);
    }

    @Override
    public void toggleRecommend(Long id, boolean isRecommend) {
        jpa.toggleRecommend(id, isRecommend);
    }

    @Override
    public void incrementViews(Long id) {
        jpa.incrementViews(id);
    }

    @Override
    public void incrementCommentCount(Long id) {
        jpa.incrementCommentCount(id);
    }

    @Override
    public void incrementBookmarkCount(Long id) {
        jpa.incrementBookmarkCount(id);
    }

    @Override
    public void decrementBookmarkCount(Long id) {
        jpa.decrementBookmarkCount(id);
    }

    @Override
    public void incrementViewsBy(Long id, long count) {
        jpa.incrementViewsBy(id, count);
    }

    @Override
    public List<Blog> search(String keyword, int limit, int offset) {
        return mybatis.search(keyword, limit, offset);
    }

    @Override
    public long countSearch(String keyword) {
        return jpa.countSearch(keyword);
    }

    @Override
    public Optional<Blog> findRandomPublished() {
        return mybatis.findRandomPublished();
    }

    @Override
    public Optional<Blog> findRandomByCategoryId(Long categoryId) {
        return mybatis.findRandomByCategoryId(categoryId);
    }
}
