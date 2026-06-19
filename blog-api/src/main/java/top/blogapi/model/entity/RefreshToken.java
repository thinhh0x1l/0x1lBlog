package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {
    Long id;
    Long userId;
    String tokenHash;
    String deviceInfo;
    String ipAddress;
    OffsetDateTime expiresAt;
    Boolean revoked;
    OffsetDateTime createdAt;
}
