package top.blogapi.engagement.notification.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.engagement.notification.domain.entity.Notification;
import top.blogapi.engagement.notification.domain.repository.NotificationRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CacheService cacheService;

    public List<Notification> getByUserId(Long userId, int page, int size) {
        return notificationRepository.findByUserId(userId, size, page * size);
    }

    public long countByUserId(Long userId) {
        return notificationRepository.countByUserId(userId);
    }

    public long countUnread(Long userId) {
        return notificationRepository.countUnread(userId);
    }

    public void create(Notification notification) {
        notificationRepository.insert(notification);
    }

    public void markRead(Long id) {
        notificationRepository.markRead(id);
    }

    public void markAllRead(Long userId) {
        notificationRepository.markAllRead(userId);
        cacheService.evict(CacheKey.notificationUnread(userId));
    }
}
