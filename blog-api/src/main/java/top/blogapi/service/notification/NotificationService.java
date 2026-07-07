package top.blogapi.service.notification;

import top.blogapi.model.entity.Notification;

import java.util.List;

/**
 * Giao diện service cho thông báo người dùng, hỗ trợ tạo,
 * truy xuất và quản lý trạng thái đã đọc/chưa đọc.
 */
public interface NotificationService {
    List<Notification> getByUserId(Long userId, int page, int size);
    long countUnread(Long userId);
    long countByUserId(Long userId);
    void create(Notification notification);
    void markRead(Long id);
    void markAllRead(Long userId);
}
