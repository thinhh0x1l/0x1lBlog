package top.blogapi.social.story.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;
import top.blogapi.shared.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "stories")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Story extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(length = 500)
    String mediaUrl;

    @Column(length = 50)
    String mediaType;

    @Column(columnDefinition = "TEXT")
    String textContent;

    @Column(length = 20)
    String visibility;

    Long viewCount;

    Instant expiresAt;
}
