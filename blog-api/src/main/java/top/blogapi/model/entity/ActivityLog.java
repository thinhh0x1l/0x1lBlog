package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Ghi lại hành động của người dùng hoặc hệ thống phục vụ kiểm toán và phân tích. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityLog {
    Long id;
    String traceId;
    Long sessionId;
    Long userId;
    String category;
    String action;
    String source;
    String targetType;
    Long targetId;
    String metadata;
    OffsetDateTime createdAt;
}
