package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.dto.mapper.BlogMapper;
import top.blogapi.dto.response.BlogResponse;
import top.blogapi.service.blog.BlogService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogAdminOrchestrator {

    private final BlogService blogService;
    private final BlogMapper blogMapper;

    public List<BlogResponse> getAll(int page, int size) {
        return blogService.getPublished(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
    }

    public void toggleTop(Long id, boolean isTop) {
        blogService.toggleTop(id, isTop);
    }

    public void toggleRecommend(Long id, boolean isRecommend) {
        blogService.toggleRecommend(id, isRecommend);
    }

    public void delete(Long id) {
        blogService.softDelete(id);
    }
}
