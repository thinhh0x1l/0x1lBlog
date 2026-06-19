package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String type;
    private String title;
    private String message;
    private String targetType;
    private Long targetId;
    private Boolean isRead;
    private OffsetDateTime createdAt;
}
