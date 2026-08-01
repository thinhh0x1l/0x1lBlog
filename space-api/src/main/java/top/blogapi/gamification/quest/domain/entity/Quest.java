package top.blogapi.gamification.quest.domain.entity;

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
@Table(name = "quests")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 50)
    String type;

    @Column(nullable = false, length = 200)
    String title;

    @Column(length = 1000)
    String description;

    @Column(columnDefinition = "TEXT")
    String conditions;

    @Column(columnDefinition = "TEXT")
    String rewards;

    @Column(nullable = false)
    Boolean isActive;

    Instant createdAt;

    Instant updatedAt;
}
