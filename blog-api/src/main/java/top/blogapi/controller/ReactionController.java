package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.ReactionOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý cảm xúc nội dung: thêm, xoá và lấy tổng quan cảm xúc.
 */
@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionOrchestrator reactionOrchestrator;

    @PostMapping
    public ResponseEntity<ApiResponse> react(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestParam String targetType,
                                              @RequestParam Long targetId,
                                              @RequestParam String type) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        reactionOrchestrator.react(targetType, targetId, principal.getId(), type);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> unreact(@AuthenticationPrincipal UserPrincipal principal,
                                                @RequestParam String targetType,
                                                @RequestParam Long targetId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        reactionOrchestrator.unreact(targetType, targetId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSummary(@RequestParam String targetType,
                                                   @RequestParam Long targetId,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        var result = reactionOrchestrator.getSummary(targetType, targetId, principal != null ? principal.getId() : null);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
