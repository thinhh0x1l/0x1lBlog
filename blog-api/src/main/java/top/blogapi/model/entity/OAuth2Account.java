package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Tài khoản OAuth2 từ nhà cung cấp bên thứ ba liên kết với người dùng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2Account {
    Long id;
    Long userId;
    String provider;
    String providerId;
    String email;
    String avatarUrl;
    String rawAttributes;
    OffsetDateTime createdAt;
}
