package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bản ghi nhật ký về sự thay đổi điểm kinh nghiệm của người dùng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserExpLog {
    Long id;
    Long userId;
    Integer amount;
    String reason;
    Long refId;
    OffsetDateTime createdAt;
}
