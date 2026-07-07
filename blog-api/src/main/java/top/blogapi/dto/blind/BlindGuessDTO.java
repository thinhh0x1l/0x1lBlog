package top.blogapi.dto.blind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DTO đại diện cho lượt đoán của người dùng trong thử thách ẩn, bao gồm tính đúng sai.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindGuessDTO {
    Long id;
    Long challengeId;
    Long guessedTopicId;
    Boolean isCorrect;
}
