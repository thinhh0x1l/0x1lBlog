package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MomentLikedByGuestIdInternal {
    Long id;
    String content;
    LocalDateTime createTime;
    Integer likes;
    boolean liked;

}
