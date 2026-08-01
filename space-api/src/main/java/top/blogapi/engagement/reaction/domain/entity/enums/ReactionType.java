package top.blogapi.engagement.reaction.domain.entity.enums;

import lombok.Getter;

@Getter
public enum ReactionType {
    LIKE("LIKE"),
    LOVE("LOVE"),
    HAHA("HAHA"),
    WOW("WOW"),
    SAD("SAD"),
    ANGRY("ANGRY");

    private final String value;

    ReactionType(String value) {
        this.value = value;
    }
}
