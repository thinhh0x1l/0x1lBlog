package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;



/** Nhiệm vụ được giao cho người dùng, theo dõi tiến trình hoàn thành. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserQuest {
    Long id;
    Long userId;
    Long questId;
    Integer progress;
    Integer target;
    String status;
    OffsetDateTime claimedAt;
    OffsetDateTime expiresAt;
    OffsetDateTime createdAt;
}
