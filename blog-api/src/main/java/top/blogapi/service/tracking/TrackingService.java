package top.blogapi.service.tracking;

import top.blogapi.model.entity.ActivityLog;
import top.blogapi.model.entity.Session;

/**
 * Giao diện service theo dõi hoạt động người dùng, quản lý phiên
 * và ghi nhật ký hoạt động cá nhân cho phân tích.
 */
public interface TrackingService {
    Session createSession(String sessionId, Long userId, String ipAddress, String userAgent, String deviceType);
    void endSession(Long sessionId);
    void trackActivity(ActivityLog log);
}
