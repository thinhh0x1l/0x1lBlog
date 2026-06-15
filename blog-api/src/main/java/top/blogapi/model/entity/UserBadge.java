package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBadge {
    Long id;
    Long userId;
    Long badgeId;
    LocalDateTime awardedAt;
    Long awardedBy;
}
