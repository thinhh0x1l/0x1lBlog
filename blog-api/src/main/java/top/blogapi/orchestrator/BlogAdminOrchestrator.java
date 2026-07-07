package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Blog;
import top.blogapi.service.blog.BlogService;

import java.util.List;

/**
 * Điều phối các thao tác blog cấp quản trị: liệt kê, ghim, đề xuất và xoá blog.
 */
@Component
@RequiredArgsConstructor
public class BlogAdminOrchestrator {

    private final BlogService blogService;

    public List<Blog> getAll(int page, int size) {
        return blogService.getPublished(page, size);
    }

    @Transactional
    public void toggleTop(Long id, boolean isTop) {
        blogService.toggleTop(id, isTop);
    }

    @Transactional
    public void toggleRecommend(Long id, boolean isRecommend) {
        blogService.toggleRecommend(id, isRecommend);
    }

    @Transactional
    public void delete(Long id) {
        blogService.softDelete(id);
    }
}
