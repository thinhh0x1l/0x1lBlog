package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.common.response.PagedResponse;
import top.blogapi.dto.response.NotificationResponse;
import top.blogapi.orchestrator.NotificationOrchestrator;
import top.blogapi.security.UserPrincipal;

import java.util.List;

/**
 * Quản lý thông báo người dùng: danh sách, đếm chưa đọc và cập nhật trạng thái đã đọc.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationOrchestrator notificationOrchestrator;

    @GetMapping
    public ResponseEntity<PagedResponse<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (principal == null) {
            return ResponseEntity.status(401).body(PagedResponse.of(List.of(), page, size, 0));
        }
        var notifications = notificationOrchestrator.getByUserId(principal.getId(), page, size);
        var total = notificationOrchestrator.countByUserId(principal.getId());
        return ResponseEntity.ok(PagedResponse.of(notifications, page, size, total));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> unreadCount(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(notificationOrchestrator.countUnread(principal.getId())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long id) {
        notificationOrchestrator.markRead(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllRead(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        notificationOrchestrator.markAllRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
