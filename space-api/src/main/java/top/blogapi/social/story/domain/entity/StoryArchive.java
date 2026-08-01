package top.blogapi.social.story.domain.entity;

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
@Table(name = "story_archives")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "story_id", nullable = false)
    Long storyId;

    @Column(length = 500)
    String mediaUrl;

    @Column(length = 50)
    String mediaType;

    @Column(columnDefinition = "TEXT")
    String textContent;

    Long viewCount;

    Instant createdAt;

    Instant archivedAt;
}
