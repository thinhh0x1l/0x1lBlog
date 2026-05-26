package top.blogapi.model.vo;

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
public class Moment {
    Long id;
    String content;
    LocalDateTime createTime;
    Integer likes;
    Boolean published;
}
