package top.blogapi.model.event;

import lombok.Value;
import top.blogapi.model.entity.Comment;

/** Sự kiện được kích hoạt khi bình luận được tạo. */
@Value
public class CommentCreatedEvent {
    Comment comment;
}
