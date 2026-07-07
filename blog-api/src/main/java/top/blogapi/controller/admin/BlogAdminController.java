package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.BlogMapper;
import top.blogapi.orchestrator.BlogAdminOrchestrator;

/**
 * Endpoint quản trị để kiểm duyệt blog: danh sách, bật/tắt trạng thái nổi bật/gợi ý và xoá.
 */
@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class BlogAdminController {

    private final BlogAdminOrchestrator blogAdminOrchestrator;
    private final BlogMapper blogMapper;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        var blogs = blogAdminOrchestrator.getAll(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @PutMapping("/{id}/top")
    public ResponseEntity<ApiResponse> toggleTop(@PathVariable Long id, @RequestParam boolean isTop) {
        blogAdminOrchestrator.toggleTop(id, isTop);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}/recommend")
    public ResponseEntity<ApiResponse> toggleRecommend(@PathVariable Long id, @RequestParam boolean isRecommend) {
        blogAdminOrchestrator.toggleRecommend(id, isRecommend);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        blogAdminOrchestrator.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
