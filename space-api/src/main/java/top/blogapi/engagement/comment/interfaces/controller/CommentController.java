package top.blogapi.engagement.comment.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.shared.response.PageResponse;
import top.blogapi.engagement.comment.interfaces.dto.CommentMapper;
import top.blogapi.engagement.comment.interfaces.dto.CommentRequest;
import top.blogapi.engagement.comment.interfaces.dto.CommentResponse;
import top.blogapi.engagement.comment.application.command.CreateCommentCommand;
import top.blogapi.engagement.comment.application.command.UpdateCommentCommand;
import top.blogapi.engagement.comment.application.command.DeleteCommentCommand;
import top.blogapi.engagement.comment.application.query.GetCommentQuery;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    @Data
    public static class UpdateCommentRequest {
        @NotBlank
        private String content;
    }

    private final CreateCommentCommand createCommentCommand;
    private final UpdateCommentCommand updateCommentCommand;
    private final DeleteCommentCommand deleteCommentCommand;
    private final GetCommentQuery getCommentQuery;
    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<PageResponse<CommentResponse>> getByTarget(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var comments = getCommentQuery.getRootByTarget(targetType, targetId, page, size).stream()
                .map(commentMapper::toResponse)
                .toList();
        var total = getCommentQuery.countRootByTarget(targetType, targetId);
        return ResponseEntity.ok(PageResponse.of(comments, page, size, total));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<ApiResponse> getReplies(@PathVariable Long id) {
        var replies = getCommentQuery.getReplies(id).stream()
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
        return ResponseEntity.ok(ApiResponse.success(createCommentCommand.execute(request, principal.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id,
                                              @Valid @RequestBody UpdateCommentRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(updateCommentCommand.execute(id, request.getContent(), principal.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        deleteCommentCommand.execute(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
