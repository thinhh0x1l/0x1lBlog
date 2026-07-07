package top.blogapi.model.entity.blind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/** Thử thách blind-box hàng ngày, người dùng đoán chủ đề ẩn từ gợi ý. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindChallenge {
    Long id;
    LocalDate date;
    Long topicId;
    String topicHint;
    String options;
    Boolean isRevealed;
    OffsetDateTime createdAt;
}
