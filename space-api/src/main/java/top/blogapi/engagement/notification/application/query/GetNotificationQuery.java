package top.blogapi.engagement.notification.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.notification.domain.service.NotificationService;
import top.blogapi.engagement.notification.interfaces.dto.NotificationMapper;
import top.blogapi.engagement.notification.interfaces.dto.NotificationResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetNotificationQuery {

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
