package top.blogapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.common.response.PagedResponse;
import top.blogapi.dto.mapper.CommentMapper;
import top.blogapi.dto.request.comment.CommentRequest;
import top.blogapi.dto.response.CommentResponse;
import top.blogapi.orchestrator.CommentOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Xử lý CRUD bình luận, trả lời theo luồng và danh sách theo phạm vi mục tiêu.
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    @Data
    public static class UpdateCommentRequest {
        @NotBlank
        private String content;
    }

    private final CommentOrchestrator commentOrchestrator;
    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<CommentResponse>> getByTarget(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var comments = commentOrchestrator.getRootByTarget(targetType, targetId, page, size).stream()
                .map(commentMapper::toResponse)
                .toList();
        var total = commentOrchestrator.countRootByTarget(targetType, targetId);
        return ResponseEntity.ok(PagedResponse.of(comments, page, size, total));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<ApiResponse> getReplies(@PathVariable Long id) {
        var replies = commentOrchestrator.getReplies(id).stream()
                .map(commentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CommentRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(commentOrchestrator.createComment(request, principal.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentOrchestrator.updateComment(id, request.getContent())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        commentOrchestrator.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
