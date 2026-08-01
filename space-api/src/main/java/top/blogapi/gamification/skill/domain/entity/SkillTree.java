package top.blogapi.gamification.skill.domain.entity;

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
@Table(name = "skill_trees")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillTree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "category_id", nullable = false)
    Long categoryId;

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 500)
    String description;

    @Column(length = 50)
    String perkType;

    @Column(length = 500)
    String perkValue;

    Integer pointsRequired;

    Integer sortOrder;

    Instant createdAt;

    Instant updatedAt;
}
