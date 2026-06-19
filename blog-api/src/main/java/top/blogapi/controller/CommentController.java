package top.blogapi.controller;

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
import top.blogapi.service.comment.CommentService;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentOrchestrator commentOrchestrator;
    private final CommentMapper commentMapper;

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<PagedResponse<CommentResponse>> getByBlog(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var comments = commentService.getRootByBlogId(blogId, page, size).stream()
                .map(commentMapper::toResponse)
                .toList();
        var total = commentService.countRootByBlogId(blogId);
        return ResponseEntity.ok(PagedResponse.of(comments, page, size, total));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<ApiResponse> getReplies(@PathVariable Long id) {
        var replies = commentService.getReplies(id).stream()
                .map(commentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody CommentRequest request) {
        Long userId = principal != null ? principal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(commentOrchestrator.createComment(request, userId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody String content) {
        return ResponseEntity.ok(ApiResponse.success(commentOrchestrator.updateComment(id, content)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        commentService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
