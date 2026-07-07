package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Chuỗi bài viết blog nhóm theo một chủ đề chung. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogSeries extends BaseEntity {
    String name;
    String description;
    String coverImage;
    Long authorId;
    String status;
    Integer price;
    Integer postCount;
    Integer subscriberCount;
}
