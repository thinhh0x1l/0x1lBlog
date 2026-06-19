package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    String username;
    String email;
    String passwordHash;
    String displayName;
    String avatarUrl;
    String bio;
    String website;
    String location;
    String socialLinks;
    String role;
    Boolean isCreator;
    String status;
    OffsetDateTime lockedUntil;
    Integer blogCount;
    Integer followerCount;
    Integer followingCount;
    Integer level;
    Long exp;
    Integer checkinStreak;
    LocalDate lastCheckinAt;
    Long balance;
    Long bonus;
    OffsetDateTime lastActiveAt;
}
