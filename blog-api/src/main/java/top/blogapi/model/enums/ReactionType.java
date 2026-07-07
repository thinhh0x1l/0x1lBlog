package top.blogapi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Các loại cảm xúc có sẵn để tương tác với nội dung. */
@Getter
@RequiredArgsConstructor
public enum ReactionType {
    LIKE("LIKE"),
    LOVE("LOVE"),
    HAHA("HAHA"),
    WOW("WOW"),
    SAD("SAD"),
    ANGRY("ANGRY");

    private final String value;
}
