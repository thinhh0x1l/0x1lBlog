package top.blogapi.dto.internal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MomentInternal {
    Long id;
    String content;
    LocalDateTime createTime;
    Integer likes;
    Boolean published;
}
