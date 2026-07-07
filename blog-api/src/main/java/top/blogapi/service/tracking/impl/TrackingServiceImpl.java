package top.blogapi.service.tracking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.ActivityLog;
import top.blogapi.model.entity.Session;
import top.blogapi.repository.ActivityLogRepository;
import top.blogapi.repository.SessionRepository;
import top.blogapi.service.tracking.TrackingService;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai TrackingService tạo và kết thúc phiên người dùng,
 * ghi nhật ký hoạt động cá nhân.
 */
public class TrackingServiceImpl implements TrackingService {

    private final SessionRepository sessionRepository;
    private final ActivityLogRepository activityLogRepository;

    @Override
    public Session createSession(String sessionId, Long userId, String ipAddress, String userAgent, String deviceType) {
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(userId);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setDeviceType(deviceType);
        sessionRepository.insert(session);
        return session;
    }

    @Override
    public void endSession(Long sessionId) {
        sessionRepository.endSession(sessionId);
    }

    @Override
    public void trackActivity(ActivityLog log) {
        activityLogRepository.insert(log);
    }
}
