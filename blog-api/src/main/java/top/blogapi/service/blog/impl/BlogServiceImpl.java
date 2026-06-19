package top.blogapi.service.blog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Blog;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.blog.BlogService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Blog create(Blog blog) {
        blogRepository.insert(blog);
        if (blog.getCategoryId() != null) {
            categoryRepository.refreshBlogCount(blog.getCategoryId());
        }
        return blog;
    }

    @Override
    @Transactional
    @CacheEvict(value = "blogs", key = "#blog.id")
    public Blog update(Blog blog) {
        blogRepository.update(blog);
        return findById(blog.getId());
    }

    @Override
    @Cacheable(value = "blogs", key = "'findById:' + #id")
    public Blog findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "blogs", key = "'findBySlug:' + #slug")
    public Blog findBySlug(String slug) {
        return blogRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
    }

    @Override
    @Transactional
    @CacheEvict(value = "blogs", key = "'findById:' + #id")
    public void softDelete(Long id) {
        blogRepository.softDelete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "blogs", key = "'findById:' + #id")
    public void publish(Long id) {
        blogRepository.updateStatus(id, "PUBLISHED");
    }

    @Override
    @Transactional
    @CacheEvict(value = "blogs", key = "'findById:' + #id")
    public void archive(Long id) {
        blogRepository.updateStatus(id, "ARCHIVED");
    }

    @Override
    @Cacheable(value = "blogs", key = "'getPublished:' + #page + ':' + #size")
    public List<Blog> getPublished(int page, int size) {
        return blogRepository.findPublished(size, page * size);
    }

    @Override
    @Cacheable(value = "blogs", key = "'getByAuthorId:' + #authorId + ':' + #page + ':' + #size")
    public List<Blog> getByAuthorId(Long authorId, int page, int size) {
        return blogRepository.findByAuthorId(authorId, size, page * size);
    }

    @Override
    @Cacheable(value = "blogs", key = "'getByCategoryId:' + #categoryId + ':' + #page + ':' + #size")
    public List<Blog> getByCategoryId(Long categoryId, int page, int size) {
        return blogRepository.findByCategoryId(categoryId, size, page * size);
    }

    @Override
    @Cacheable(value = "blogs", key = "'getTrending:' + #limit")
    public List<Blog> getTrending(int limit) {
        return blogRepository.findTrending(limit);
    }

    @Override
    @Cacheable(value = "blogs", key = "'getRecommended:' + #limit")
    public List<Blog> getRecommended(int limit) {
        return blogRepository.findRecommended(limit);
    }

    @Override
    @Cacheable(value = "blogs", key = "'search:' + #keyword + ':' + #page + ':' + #size")
    public List<Blog> search(String keyword, int page, int size) {
        return blogRepository.search(keyword, size, page * size);
    }

    @Override
    @Cacheable(value = "blogs", key = "'countPublished'")
    public long countPublished() {
        return blogRepository.countPublished();
    }

    @Override
    @Cacheable(value = "blogs", key = "'countByAuthorId:' + #authorId")
    public long countByAuthorId(Long authorId) {
        return blogRepository.countByAuthorId(authorId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "blogs", key = "'findById:' + #id")
    public void incrementViews(Long id) {
        blogRepository.incrementViews(id);
    }

    @Override
    @Transactional
    public void updateCategoryCounters(Long categoryId) {
        categoryRepository.refreshBlogCount(categoryId);
    }
}
