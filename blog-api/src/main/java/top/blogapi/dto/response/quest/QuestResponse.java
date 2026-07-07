package top.blogapi.dto.response.quest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho định nghĩa nhiệm vụ với điều kiện và phần thưởng.
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestResponse {
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
