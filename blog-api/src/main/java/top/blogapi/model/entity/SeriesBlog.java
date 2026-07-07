package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Liên kết nhiều-nhiều giữa chuỗi bài và các bài viết blog thành phần. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesBlog {
    Long seriesId;
    Long blogId;
    Integer sortOrder;
    String note;
    OffsetDateTime createdAt;
}
