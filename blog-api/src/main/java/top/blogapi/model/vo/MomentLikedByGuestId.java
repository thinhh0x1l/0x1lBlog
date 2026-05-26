package top.blogapi.model.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MomentLikedByGuestId {
    Long id;
    String content;
    LocalDateTime createTime;
    Integer likes;
    boolean liked;

}
