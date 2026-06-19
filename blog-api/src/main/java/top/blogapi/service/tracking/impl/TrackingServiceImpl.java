package top.blogapi.service.tracking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.ActivityLog;
import top.blogapi.model.entity.Session;
import top.blogapi.repository.ActivityLogRepository;
import top.blogapi.repository.SessionRepository;
import top.blogapi.service.tracking.TrackingService;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final SessionRepository sessionRepository;
    private final ActivityLogRepository activityLogRepository;

    @Override
    @Transactional
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
    @Transactional
    public void endSession(Long sessionId) {
        sessionRepository.endSession(sessionId);
    }

    @Override
    @Transactional
    public void trackActivity(ActivityLog log) {
        activityLogRepository.insert(log);
    }
}
