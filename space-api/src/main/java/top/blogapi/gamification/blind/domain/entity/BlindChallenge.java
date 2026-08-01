package top.blogapi.gamification.blind.domain.entity;

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
import java.time.LocalDate;

@Entity
@Table(name = "blind_challenges")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDate date;

    @Column(name = "topic_id")
    Long topicId;

    @Column(length = 200)
    String topicHint;

    @Column(columnDefinition = "TEXT")
    String options;

    @Column(nullable = false)
    Boolean isRevealed;

    Instant createdAt;
}
