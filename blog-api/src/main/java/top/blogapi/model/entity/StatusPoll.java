package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bình chọn đính kèm với cập nhật trạng thái, gồm câu hỏi và nhiều lựa chọn. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusPoll {
    Long id;
    Long statusId;
    String question;
    String options;
    OffsetDateTime endsAt;
    OffsetDateTime createdAt;
}
