package top.blogapi.model.entity.skill;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Điểm kỹ năng tích lũy của người dùng trong một danh mục kỹ năng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSkillProgress {
    Long id;
    Long userId;
    Long categoryId;
    Integer totalPoints;
}
