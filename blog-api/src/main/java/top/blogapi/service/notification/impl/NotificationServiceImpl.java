package top.blogapi.service.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.Notification;
import top.blogapi.repository.NotificationRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.notification.NotificationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai NotificationService cung cấp CRUD thông báo người dùng
 * và xóa cache khi cập nhật số lượng chưa đọc.
 */
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CacheService cacheService;

    @Override
    public List<Notification> getByUserId(Long userId, int page, int size) {
        return notificationRepository.findByUserId(userId, size, page * size);
    }

    @Override
    public long countByUserId(Long userId) {
        return notificationRepository.countByUserId(userId);
    }

    @Override
    public long countUnread(Long userId) {
        return notificationRepository.countUnread(userId);
    }

    @Override
    public void create(Notification notification) {
        notificationRepository.insert(notification);
    }

    @Override
    public void markRead(Long id) {
        notificationRepository.markRead(id);
    }

    @Override
    public void markAllRead(Long userId) {
        notificationRepository.markAllRead(userId);
        cacheService.evict(CacheKey.notificationUnread(userId));
    }
}
