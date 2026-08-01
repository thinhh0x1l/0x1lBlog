package top.blogapi.engagement.notification.domain.entity;

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
@Table(name = "notifications")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "actor_id")
    Long actorId;

    @Column(nullable = false, length = 50)
    String type;

    @Column(nullable = false, length = 200)
    String title;

    @Column(columnDefinition = "TEXT")
    String message;

    @Column(name = "target_type", length = 20)
    String targetType;

    @Column(name = "target_id")
    Long targetId;

    @Column(nullable = false)
    Boolean isRead;

    Instant createdAt;
}
