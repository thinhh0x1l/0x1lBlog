package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {
    Long id;
    Long userId;
    String token;
    LocalDateTime expiresAt;
    LocalDateTime createdAt;
    boolean revoked;
}
