package top.blogapi.engagement.notification.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.shared.response.PageResponse;
import top.blogapi.engagement.notification.interfaces.dto.NotificationResponse;
import top.blogapi.engagement.notification.application.query.GetNotificationQuery;
import top.blogapi.infra.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final GetNotificationQuery getNotificationQuery;

    @GetMapping
    public ResponseEntity<PageResponse<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (principal == null) {
            return ResponseEntity.status(401).body(PageResponse.of(List.of(), page, size, 0));
        }
        var notifications = getNotificationQuery.getByUserId(principal.getId(), page, size);
        var total = getNotificationQuery.countByUserId(principal.getId());
        return ResponseEntity.ok(PageResponse.of(notifications, page, size, total));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> unreadCount(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getNotificationQuery.countUnread(principal.getId())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long id) {
        getNotificationQuery.markRead(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllRead(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        getNotificationQuery.markAllRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
