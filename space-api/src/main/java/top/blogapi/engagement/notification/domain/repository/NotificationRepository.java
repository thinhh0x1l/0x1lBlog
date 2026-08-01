package top.blogapi.engagement.notification.domain.repository;

import top.blogapi.engagement.notification.domain.entity.Notification;

import java.util.List;

public interface NotificationRepository {

    List<Notification> findByUserId(Long userId, int limit, int offset);

    long countUnread(Long userId);

    long countByUserId(Long userId);

    void insert(Notification notification);

    void markAllRead(Long userId);

    void markRead(Long id);

    void deleteOlderThan90Days();
}
