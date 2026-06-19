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
public class Mention {
    Long id;
    Long targetUserId;
    Long mentionedBy;
    String sourceType;
    Long sourceId;
    OffsetDateTime createdAt;
}
