package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Phiên người dùng theo dõi hoạt động đăng nhập, IP và thời lượng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {
    Long id;
    String sessionId;
    Long userId;
    String ipAddress;
    String userAgent;
    String deviceType;
    String countryCode;
    String city;
    OffsetDateTime startedAt;
    OffsetDateTime endedAt;
    Integer durationSeconds;
}
