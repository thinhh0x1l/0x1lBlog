package top.blogapi.model.event;

import lombok.Value;
import top.blogapi.model.entity.Comment;

@Value
public class CommentCreatedEvent {
    Comment comment;
}
