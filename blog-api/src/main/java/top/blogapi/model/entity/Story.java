package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bài story tạm thời với nội dung đa phương tiện, hết hạn sau một khoảng thời gian. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Story extends BaseEntity {
    Long userId;
    String mediaUrl;
    String mediaType;
    String textContent;
    String visibility;
    Long viewCount;
    OffsetDateTime expiresAt;
}
