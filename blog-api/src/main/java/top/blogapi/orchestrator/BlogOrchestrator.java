package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.blog.CreateBlogRequest;
import top.blogapi.dto.request.blog.UpdateBlogRequest;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.event.BlogPublishedEvent;
import top.blogapi.service.blog.BlogService;
import top.blogapi.service.tracking.ViewTrackingService;

import java.util.List;

/**
 * Orchestrates blog CRUD, publishing workflow, search, trending, and view tracking.
 */
@Component
@RequiredArgsConstructor
public class BlogOrchestrator {

    private final BlogService blogService;
    private final ApplicationEventPublisher eventPublisher;
    private final ViewTrackingService viewTrackingService;

    @Transactional
    public Blog createBlog(CreateBlogRequest request, Long authorId) {
        Blog blog = new Blog();
        blog.setAuthorId(authorId);
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setDescription(request.getDescription());
        blog.setCoverImage(request.getCoverImage());
        blog.setSlug(request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        blog.setCategoryId(request.getCategoryId());
        blog.setContentType(request.getContentType() != null ? request.getContentType() : "MARKDOWN");
        blog.setStatus("DRAFT");
        blog.setVisibility("PUBLIC");
        blog.setAllowComments(true);
        blog = blogService.create(blog);

        if (request.getHashtags() != null && !request.getHashtags().isEmpty()) {
            blogService.linkHashtags(blog.getId(), request.getHashtags());
        }

        return blog;
    }

    @Transactional
    public Blog updateBlog(Long id, UpdateBlogRequest request) {
        Blog blog = blogService.findById(id);
        if (request.getTitle() != null) blog.setTitle(request.getTitle());
        if (request.getContent() != null) blog.setContent(request.getContent());
        if (request.getDescription() != null) blog.setDescription(request.getDescription());
        if (request.getCoverImage() != null) blog.setCoverImage(request.getCoverImage());
        if (request.getCategoryId() != null) blog.setCategoryId(request.getCategoryId());
        if (request.getContentType() != null) blog.setContentType(request.getContentType());
        if (request.getAllowComments() != null) blog.setAllowComments(request.getAllowComments());
        blog = blogService.update(blog);

        blogService.evictCache(id);
        return blog;
    }

    @Transactional
    public Blog publishBlog(Long id) {
        blogService.publish(id);
        Blog blog = blogService.findById(id);
        eventPublisher.publishEvent(new BlogPublishedEvent(blog));
        blogService.evictCache(id);
        return blog;
    }

    @Transactional
    public void deleteBlog(Long id) {
        blogService.softDelete(id);
        blogService.evictCache(id);
    }

    public Blog getBlog(Long id) {
        return blogService.findById(id);
    }

    public Blog getBlogBySlug(String slug) {
        return blogService.findBySlug(slug);
    }

    public List<Blog> getPublished(int page, int size) {
        return blogService.getPublished(page, size);
    }

    public long countPublished() {
        return blogService.countPublished();
    }

    public List<Blog> getTrending(int limit) {
        return blogService.getTrending(limit);
    }

    public long countSearch(String keyword) {
        return blogService.countSearch(keyword);
    }

    public List<Blog> search(String keyword, int page, int size) {
        return blogService.search(keyword, page, size);
    }

    public List<Blog> getByAuthor(Long authorId, int page, int size) {
        return blogService.getByAuthorId(authorId, page, size);
    }

    public long countByAuthor(Long authorId) {
        return blogService.countByAuthorId(authorId);
    }

    public void incrementView(Long blogId, String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            blogService.incrementViews(blogId);
            return;
        }
        boolean unique = viewTrackingService.isUniqueView(sessionId, blogId);
        if (unique) {
            blogService.incrementViews(blogId);
        }
    }
}
