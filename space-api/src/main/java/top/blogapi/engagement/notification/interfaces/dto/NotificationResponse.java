package top.blogapi.engagement.notification.interfaces.dto;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String type,
        String title,
        String message,
        String targetType,
        Long targetId,
        Boolean isRead,
        Instant createdAt
) {}
