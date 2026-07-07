package top.blogapi.service.blog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.BlogHashtag;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.repository.BlogHashtagRepository;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.blog.BlogService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.hashtag.HashtagService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai BlogService với hỗ trợ cache qua CacheService,
 * xử lý CRUD blog, xuất bản, tìm kiếm và liên kết hashtag.
 */
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final BlogHashtagRepository blogHashtagRepository;
    private final HashtagService hashtagService;
    private final CacheService cacheService;

    @Override
    public Blog create(Blog blog) {
        blogRepository.insert(blog);
        if (blog.getCategoryId() != null) {
            categoryRepository.refreshBlogCount(blog.getCategoryId());
        }
        return blog;
    }

    @Override
    public Blog update(Blog blog) {
        blogRepository.update(blog);
        Blog updated = findById(blog.getId());
        evictCache(updated.getId());
        if (updated.getSlug() != null) {
            cacheService.evict(CacheKey.blogSlug(updated.getSlug()));
        }
        return updated;
    }

    @Override
    public Blog findById(Long id) {
        return cacheService.get(
                CacheKey.blog(id),
                Blog.class,
                () -> blogRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND)),
                CachePolicies.BLOG
        );
    }

    @Override
    public Blog findBySlug(String slug) {
        return cacheService.get(
                CacheKey.blogSlug(slug),
                Blog.class,
                () -> blogRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND)),
                CachePolicies.BLOG_SLUG
        );
    }

    @Override
    public void softDelete(Long id) {
        blogRepository.softDelete(id);
        evictCache(id);
    }

    @Override
    public void publish(Long id) {
        blogRepository.updateStatus(id, "PUBLISHED");
        evictCache(id);
    }

    @Override
    public void archive(Long id) {
        blogRepository.updateStatus(id, "ARCHIVED");
        evictCache(id);
    }

    @Override
    public List<Blog> getPublished(int page, int size) {
        return blogRepository.findPublished(size, page * size);
    }

    @Override
    public List<Blog> getByAuthorId(Long authorId, int page, int size) {
        return blogRepository.findByAuthorId(authorId, size, page * size);
    }

    @Override
    public List<Blog> getByCategoryId(Long categoryId, int page, int size) {
        return blogRepository.findByCategoryId(categoryId, size, page * size);
    }

    @Override
    public List<Blog> getTrending(int limit) {
        return blogRepository.findTrending(limit);
    }

    @Override
    public List<Blog> getRecommended(int limit) {
        return blogRepository.findRecommended(limit);
    }

    @Override
    public List<Blog> search(String keyword, int page, int size) {
        return blogRepository.search(keyword, size, page * size);
    }

    @Override
    public long countSearch(String keyword) {
        return blogRepository.countSearch(keyword);
    }

    @Override
    public long countPublished() {
        return blogRepository.countPublished();
    }

    @Override
    public long countByAuthorId(Long authorId) {
        return blogRepository.countByAuthorId(authorId);
    }

    @Override
    public void incrementViews(Long id) {
        blogRepository.incrementViews(id);
    }

    @Override
    public void updateCategoryCounters(Long categoryId) {
        categoryRepository.refreshBlogCount(categoryId);
    }

    @Override
    public void toggleTop(Long id, boolean isTop) {
        blogRepository.toggleTop(id, isTop);
        evictCache(id);
    }

    @Override
    public void toggleRecommend(Long id, boolean isRecommend) {
        blogRepository.toggleRecommend(id, isRecommend);
        evictCache(id);
    }

    @Override
    public void linkHashtags(Long blogId, List<String> hashtagNames) {
        List<BlogHashtag> links = new ArrayList<>();
        for (String tagName : hashtagNames) {
            Hashtag tag = hashtagService.findOrCreate(tagName);
            BlogHashtag bh = new BlogHashtag();
            bh.setBlogId(blogId);
            bh.setHashtagId(tag.getId());
            links.add(bh);
        }
        if (!links.isEmpty()) {
            blogHashtagRepository.insertBatch(links);
        }
    }

    @Override
    public void evictCache(Long id) {
        cacheService.evict(CacheKey.blog(id));
    }
}
