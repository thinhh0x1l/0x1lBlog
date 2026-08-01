package top.blogapi.content.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.service.BlogService;
import top.blogapi.content.blog.domain.service.ViewTrackingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBlogQuery {

    private final BlogService blogService;
    private final ViewTrackingService viewTrackingService;

    public Blog execute(Long id) {
        return blogService.findById(id);
    }

    public Blog getBySlug(String slug) {
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
