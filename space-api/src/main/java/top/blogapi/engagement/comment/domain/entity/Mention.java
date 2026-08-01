package top.blogapi.engagement.comment.domain.entity;

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
@Table(name = "mentions")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "target_user_id", nullable = false)
    Long targetUserId;

    @Column(name = "mentioned_by", nullable = false)
    Long mentionedBy;

    @Column(name = "source_type", nullable = false, length = 20)
    String sourceType;

    @Column(name = "source_id", nullable = false)
    Long sourceId;

    Instant createdAt;
}
