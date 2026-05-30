package top.blogapi.dto.response.moment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MomentPublished {
    Long id;
    String content;
    LocalDateTime createTime;
    Integer likes;
}

