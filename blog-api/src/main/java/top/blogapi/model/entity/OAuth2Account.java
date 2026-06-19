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
public class OAuth2Account {
    Long id;
    Long userId;
    String provider;
    String providerId;
    String avatarUrl;
    String rawAttributes;
    OffsetDateTime createdAt;
}
