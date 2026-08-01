package top.blogapi.user.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "oauth2_accounts")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(nullable = false, length = 50)
    String provider;

    @Column(name = "provider_id", nullable = false, length = 255)
    String providerId;

    @Column(length = 255)
    String email;

    @Column(length = 500)
    String avatarUrl;

    @Column(columnDefinition = "TEXT")
    String rawAttributes;

    Instant createdAt;
}
