package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.NotificationMapper;
import top.blogapi.dto.response.NotificationResponse;
import top.blogapi.service.notification.NotificationService;

import java.util.List;

/**
 * Orchestrates notification retrieval, read status management, and unread counting.
 */
@Component
@RequiredArgsConstructor
public class NotificationOrchestrator {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public List<NotificationResponse> getByUserId(Long userId, int page, int size) {
        return notificationService.getByUserId(userId, page, size).stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    public long countByUserId(Long userId) {
        return notificationService.countByUserId(userId);
    }

    public long countUnread(Long userId) {
        return notificationService.countUnread(userId);
    }

    @Transactional
    public void markRead(Long id) {
        notificationService.markRead(id);
    }

    @Transactional
    public void markAllRead(Long userId) {
        notificationService.markAllRead(userId);
    }
}
