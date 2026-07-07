package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Mẫu nhiệm vụ được xác định trước với điều kiện và phần thưởng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quest {
    Long id;
    String type;
    String title;
    String description;
    String conditions;
    String rewards;
    Boolean isActive;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
