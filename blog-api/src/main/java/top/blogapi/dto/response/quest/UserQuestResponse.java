package top.blogapi.dto.response.quest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho tiến độ của người dùng trên một nhiệm vụ cụ thể.
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserQuestResponse {
    Long id;
    Long questId;
    String questTitle;
    String questType;
    Integer progress;
    Integer target;
    String status;
    OffsetDateTime claimedAt;
    OffsetDateTime expiresAt;
}
