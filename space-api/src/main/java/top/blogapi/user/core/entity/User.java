package top.blogapi.user.core.entity;

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
import java.time.LocalDate;

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Column(nullable = false, length = 255)
    String email;

    @Column(name = "password_hash", length = 255)
    String passwordHash;

    @Column(name = "display_name", length = 100)
    String displayName;

    @Column(length = 500)
    String avatarUrl;

    @Column(length = 1000)
    String bio;

    @Column(length = 255)
    String website;

    @Column(length = 100)
    String location;

    @Column(columnDefinition = "TEXT")
    String socialLinks;

    @Column(length = 20)
    String role;

    Boolean isCreator;

    @Column(length = 20)
    String status;

    Instant lockedUntil;

    Integer blogCount;

    Integer followerCount;

    Integer followingCount;

    Integer level;

    Long exp;

    Integer checkinStreak;

    LocalDate lastCheckinAt;

    Long balance;

    Long bonus;

    Long coins;

    Long gems;

    Long reputationWriting;

    Long reputationCommunity;

    Long reputationCreativity;

    Long reputationInfluence;

    Instant lastActiveAt;

    @Column(length = 20)
    String profileLayout;

    Boolean gameMode;
}
