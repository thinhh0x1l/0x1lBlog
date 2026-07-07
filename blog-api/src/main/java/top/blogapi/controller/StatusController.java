package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.common.response.PagedResponse;
import top.blogapi.dto.request.status.StatusRequest;
import top.blogapi.dto.response.StatusResponse;
import top.blogapi.orchestrator.StatusOrchestrator;
import top.blogapi.security.UserPrincipal;

import java.util.Map;

/**
 * Xử lý cập nhật trạng thái kèm bình chọn, phần theo luồng và bỏ phiếu.
 */
@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusOrchestrator statusOrchestrator;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody StatusRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var status = statusOrchestrator.createStatus(request, principal.getId());
        var response = statusOrchestrator.buildStatusResponse(status, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var status = statusOrchestrator.getStatus(id);
        var response = statusOrchestrator.buildStatusResponse(status, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<StatusResponse>> getByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var statuses = statusOrchestrator.getByUser(userId, page, size).stream()
                .map(s -> statusOrchestrator.buildStatusResponse(s, currentUserId))
                .toList();
        var total = statusOrchestrator.countByUserId(userId);
        return ResponseEntity.ok(PagedResponse.of(statuses, page, size, total));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse> getFeed(@AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var statuses = statusOrchestrator.getFeed(20).stream()
                .map(s -> statusOrchestrator.buildStatusResponse(s, currentUserId))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(statuses));
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<ApiResponse> getThreadParts(@PathVariable Long threadId,
                                                      @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var parts = statusOrchestrator.getThreadParts(threadId).stream()
                .map(s -> statusOrchestrator.buildStatusResponse(s, currentUserId))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(parts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        statusOrchestrator.deleteStatus(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/poll/vote")
    public ResponseEntity<ApiResponse> vote(@PathVariable Long id,
                                            @AuthenticationPrincipal UserPrincipal principal,
                                            @RequestBody Map<String, Integer> body) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var optionIndex = body.get("optionIndex");
        if (optionIndex == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("optionIndex is required"));
        }
        statusOrchestrator.castVote(id, principal.getId(), optionIndex);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
