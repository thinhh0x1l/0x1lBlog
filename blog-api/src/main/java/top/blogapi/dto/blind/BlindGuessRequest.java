package top.blogapi.dto.blind;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlindGuessRequest {
    @NotNull(message = "Guessed topic ID is required")
    Long guessedTopicId;
}
