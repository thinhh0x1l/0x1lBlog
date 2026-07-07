package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Dấu trang của người dùng trên bài viết blog, có thể phân loại vào bộ sưu tập. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bookmark {
    Long id;
    Long userId;
    Long blogId;
    String collection;
    String note;
    Boolean isPublic;
    OffsetDateTime createdAt;
}
