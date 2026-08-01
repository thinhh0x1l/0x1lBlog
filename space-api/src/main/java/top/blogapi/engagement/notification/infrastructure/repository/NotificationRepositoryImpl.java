package top.blogapi.engagement.notification.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.notification.domain.entity.Notification;
import top.blogapi.engagement.notification.domain.repository.NotificationRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpa;

    @Override
    public List<Notification> findByUserId(Long userId, int limit, int offset) {
        return jpa.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(offset / limit, limit));
    }

    @Override
    public long countUnread(Long userId) {
        return jpa.countUnreadByUserId(userId);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpa.countByUserId(userId);
    }

    @Override
    public void insert(Notification notification) {
        jpa.save(notification);
    }

    @Override
    public void markAllRead(Long userId) {
        jpa.markAllReadByUserId(userId);
    }

    @Override
    public void markRead(Long id) {
        jpa.markReadById(id);
    }

    @Override
    public void deleteOlderThan90Days() {
        jpa.deleteOlderThan90Days();
    }
}
