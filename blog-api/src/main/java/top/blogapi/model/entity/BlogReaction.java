package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Cảm xúc (thích, yêu thích, v.v.) người dùng để lại trên bài viết blog. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogReaction {
    Long id;
    Long userId;
    Long blogId;
    String type;
    OffsetDateTime createdAt;
}
