package top.blogapi.model.entity.skill;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Nút kỹ năng trong cây kỹ năng, ban tặng đặc quyền khi đầu tư đủ điểm. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillTree {
    Long id;
    Long categoryId;
    String name;
    String description;
    String perkType;
    String perkValue;
    Integer pointsRequired;
    Integer sortOrder;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
