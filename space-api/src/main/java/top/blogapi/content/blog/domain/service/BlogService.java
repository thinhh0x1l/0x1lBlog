package top.blogapi.content.blog.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.entity.BlogHashtag;
import top.blogapi.content.hashtag.domain.entity.Hashtag;
import top.blogapi.content.blog.domain.repository.BlogHashtagRepository;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.content.category.domain.repository.CategoryRepository;
import top.blogapi.content.hashtag.domain.service.HashtagService;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final BlogHashtagRepository blogHashtagRepository;
    private final HashtagService hashtagService;
    private final CacheService cacheService;

    public Blog create(Blog blog) {
        blogRepository.save(blog);
        if (blog.getCategoryId() != null) {
            categoryRepository.refreshBlogCount(blog.getCategoryId());
        }
        return blog;
    }

    public Blog update(Blog blog) {
        blogRepository.save(blog);
        Blog updated = findById(blog.getId());
        evictCache(updated.getId());
        if (updated.getSlug() != null) {
            cacheService.evict(CacheKey.blogSlug(updated.getSlug()));
        }
        return updated;
    }

    public Blog findById(Long id) {
        return cacheService.get(
                CacheKey.blog(id),
                Blog.class,
                () -> blogRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND)),
                CachePolicies.BLOG
        );
    }

    public Blog findBySlug(String slug) {
        return cacheService.get(
                CacheKey.blogSlug(slug),
                Blog.class,
                () -> blogRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND)),
                CachePolicies.BLOG_SLUG
        );
    }

    public void softDelete(Long id) {
        blogRepository.softDelete(id);
        evictCache(id);
    }

    public void publish(Long id) {
        blogRepository.updateStatus(id, "PUBLISHED");
        evictCache(id);
    }

    public void archive(Long id) {
        blogRepository.updateStatus(id, "ARCHIVED");
        evictCache(id);
    }

    public List<Blog> getPublished(int page, int size) {
        return blogRepository.findPublished(size, page * size);
    }

    public List<Blog> getByAuthorId(Long authorId, int page, int size) {
        return blogRepository.findByAuthorId(authorId, size, page * size);
    }

    public List<Blog> getByCategoryId(Long categoryId, int page, int size) {
        return blogRepository.findByCategoryId(categoryId, size, page * size);
    }

    public List<Blog> getTrending(int limit) {
        return blogRepository.findTrending(limit);
    }

    public List<Blog> getRecommended(int limit) {
        return blogRepository.findRecommended(limit);
    }

    public List<Blog> search(String keyword, int page, int size) {
        return blogRepository.search(keyword, size, page * size);
    }

    public long countSearch(String keyword) {
        return blogRepository.countSearch(keyword);
    }

    public long countPublished() {
        return blogRepository.countPublished();
    }

    public long countByAuthorId(Long authorId) {
        return blogRepository.countByAuthorId(authorId);
    }

    public void incrementViews(Long id) {
        blogRepository.incrementViews(id);
    }

    public void updateCategoryCounters(Long categoryId) {
        categoryRepository.refreshBlogCount(categoryId);
    }

    public void toggleTop(Long id, boolean isTop) {
        blogRepository.toggleTop(id, isTop);
        evictCache(id);
    }

    public void toggleRecommend(Long id, boolean isRecommend) {
        blogRepository.toggleRecommend(id, isRecommend);
        evictCache(id);
    }

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
            blogHashtagRepository.saveBatch(links);
        }
    }

    public void evictCache(Long id) {
        cacheService.evict(CacheKey.blog(id));
    }
}
