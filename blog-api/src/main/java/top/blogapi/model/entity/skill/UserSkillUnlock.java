package top.blogapi.model.entity.skill;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bản ghi nút kỹ năng được người dùng mở khóa trong cây kỹ năng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSkillUnlock {
    Long id;
    Long userId;
    Long skillId;
    OffsetDateTime unlockedAt;
}
