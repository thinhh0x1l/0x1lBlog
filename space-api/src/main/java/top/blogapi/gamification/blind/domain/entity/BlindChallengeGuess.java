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

@Entity
@Table(name = "blind_challenge_guesses")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindChallengeGuess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "challenge_id", nullable = false)
    Long challengeId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "guessed_topic_id")
    Long guessedTopicId;

    @Column(nullable = false)
    Boolean isCorrect;

    Instant createdAt;
}
