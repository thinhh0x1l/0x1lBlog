package top.blogapi.dto.blind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/**
 * DTO đại diện cho một mục trên bảng xếp hạng của thử thách ẩn.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindLeaderboardDTO {
    Long userId;
    OffsetDateTime guessedAt;
}
