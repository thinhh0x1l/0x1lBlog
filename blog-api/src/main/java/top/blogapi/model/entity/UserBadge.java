package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Huy hiệu người dùng đạt được, liên kết với người trao. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBadge {
    Long id;
    Long userId;
    Long badgeId;
    Long awardedBy;
    OffsetDateTime awardedAt;
}
