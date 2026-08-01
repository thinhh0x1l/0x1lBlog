package top.blogapi.engagement.comment.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.engagement.comment.domain.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toResponse(Comment comment);
}
