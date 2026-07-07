package top.blogapi.dto.blind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO đại diện cho thử thách ẩn với gợi ý chủ đề, tùy chọn và trạng thái tiết lộ.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindChallengeDTO {
    Long id;
    LocalDate date;
    String topicHint;
    List<Map<String, Object>> options;
    Boolean revealed;
    OffsetDateTime createdAt;
}
