package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Hashtag dùng để gắn nhãn và khám phá bài viết blog. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hashtag {
    Long id;
    String name;
    Integer usageCount;
    OffsetDateTime createdAt;
}
