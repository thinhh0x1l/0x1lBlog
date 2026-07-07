package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Phiếu bầu của người dùng cho một lựa chọn trong bình chọn trạng thái. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusPollVote {
    Long id;
    Long pollId;
    Long userId;
    Integer optionIndex;
    OffsetDateTime createdAt;
}
