package top.blogapi.service.notification.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Notification;
import top.blogapi.repository.NotificationRepository;
import top.blogapi.service.notification.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Cacheable(value = "notifications", key = "'getByUserId:' + #userId + ':' + #page + ':' + #size")
    public List<Notification> getByUserId(Long userId, int page, int size) {
        return notificationRepository.findByUserId(userId, size, page * size);
    }

    @Override
    @Cacheable(value = "notifications", key = "'countUnread:' + #userId")
    public long countUnread(Long userId) {
        return notificationRepository.countUnread(userId);
    }

    @Override
    @Transactional
    public void create(Notification notification) {
        notificationRepository.insert(notification);
    }

    @Override
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public void markRead(Long id) {
        notificationRepository.markRead(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "notifications", allEntries = true)
    public void markAllRead(Long userId) {
        notificationRepository.markAllRead(userId);
    }
}
