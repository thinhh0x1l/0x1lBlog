package top.blogapi.dto.response.blog;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogSummaryResponse {
    Long id;
    String title;
    String categoryName;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    boolean recommend;
    boolean published;
}
