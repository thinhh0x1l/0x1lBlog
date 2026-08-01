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
@Table(name = "sessions")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String sessionId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(length = 50)
    String ipAddress;

    @Column(length = 500)
    String userAgent;

    @Column(length = 50)
    String deviceType;

    @Column(length = 10)
    String countryCode;

    @Column(length = 100)
    String city;

    Instant startedAt;

    Instant endedAt;

    Integer durationSeconds;
}
