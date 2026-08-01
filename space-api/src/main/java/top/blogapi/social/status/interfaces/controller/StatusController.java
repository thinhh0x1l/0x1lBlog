package top.blogapi.social.status.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.shared.response.PageResponse;
import top.blogapi.social.status.interfaces.dto.StatusRequest;
import top.blogapi.social.status.interfaces.dto.StatusResponse;
import top.blogapi.social.status.application.command.CreateStatusCommand;
import top.blogapi.social.status.application.command.VoteStatusCommand;
import top.blogapi.social.status.application.query.GetStatusQuery;
import top.blogapi.infra.security.UserPrincipal;

import java.util.Map;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final CreateStatusCommand createStatusCommand;
    private final VoteStatusCommand voteStatusCommand;
    private final GetStatusQuery getStatusQuery;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody StatusRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var status = createStatusCommand.execute(request, principal.getId());
        var response = getStatusQuery.buildStatusResponse(status, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var status = getStatusQuery.execute(id);
        var response = getStatusQuery.buildStatusResponse(status, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<StatusResponse>> getByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var statuses = getStatusQuery.getByUser(userId, page, size).stream()
                .map(s -> getStatusQuery.buildStatusResponse(s, currentUserId))
                .toList();
        var total = getStatusQuery.countByUserId(userId);
        return ResponseEntity.ok(PageResponse.of(statuses, page, size, total));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse> getFeed(@AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var statuses = getStatusQuery.getFeed(20).stream()
                .map(s -> getStatusQuery.buildStatusResponse(s, currentUserId))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(statuses));
    }

    @GetMapping("/thread/{threadId}")
    public ResponseEntity<ApiResponse> getThreadParts(@PathVariable Long threadId,
                                                      @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        var parts = getStatusQuery.getThreadParts(threadId).stream()
                .map(s -> getStatusQuery.buildStatusResponse(s, currentUserId))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(parts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        getStatusQuery.deleteStatus(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/poll/vote")
    public ResponseEntity<ApiResponse> vote(@PathVariable Long id,
                                            @AuthenticationPrincipal UserPrincipal principal,
                                            @RequestBody Map<String, Integer> body) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        Integer optionIndex = body.get("optionIndex");
        if (optionIndex == null || optionIndex < 0) {
            return ResponseEntity.badRequest().body(ApiResponse.error("optionIndex is required and must be >= 0"));
        }
        voteStatusCommand.execute(id, principal.getId(), optionIndex);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
