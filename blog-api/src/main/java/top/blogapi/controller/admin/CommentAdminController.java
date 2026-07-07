package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.CommentAdminOrchestrator;

/**
 * Endpoint quản trị để kiểm duyệt bình luận: phê duyệt, từ chối và xoá.
 */
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentAdminOrchestrator commentAdminOrchestrator;

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approve(@PathVariable Long id) {
        commentAdminOrchestrator.approve(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> reject(@PathVariable Long id) {
        commentAdminOrchestrator.reject(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        commentAdminOrchestrator.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
