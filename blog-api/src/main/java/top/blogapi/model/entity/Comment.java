package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bình luận trên bài viết blog hoặc nội dung khác, hỗ trợ trả lời theo luồng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    String targetType;
    Long targetId;
    Long parentId;
    Long userId;
    String authorName;
    String authorAvatar;
    String content;
    String status;
    Integer replyCount;
}
