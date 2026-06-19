package top.blogapi.service.notification;

import top.blogapi.model.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getByUserId(Long userId, int page, int size);
    long countUnread(Long userId);
    void create(Notification notification);
    void markRead(Long id);
    void markAllRead(Long userId);
}
