package top.blogapi.service.tracking;

import top.blogapi.model.entity.ActivityLog;
import top.blogapi.model.entity.Session;

public interface TrackingService {
    Session createSession(String sessionId, Long userId, String ipAddress, String userAgent, String deviceType);
    void endSession(Long sessionId);
    void trackActivity(ActivityLog log);
}
