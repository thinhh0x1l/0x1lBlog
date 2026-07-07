package top.blogapi.model.entity.blind;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bài dự đoán của người dùng cho thử thách blind. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindChallengeGuess {
    Long id;
    Long challengeId;
    Long userId;
    Long guessedTopicId;
    Boolean isCorrect;
    OffsetDateTime createdAt;
}
