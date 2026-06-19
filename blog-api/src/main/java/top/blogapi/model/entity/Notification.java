package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    Long id;
    Long userId;
    Long actorId;
    String type;
    String title;
    String message;
    String targetType;
    Long targetId;
    Boolean isRead;
    OffsetDateTime createdAt;
}
