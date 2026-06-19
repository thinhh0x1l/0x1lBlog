package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.CommentResponse;
import top.blogapi.model.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", ignore = true)
    @Mapping(target = "authorAvatar", ignore = true)
    CommentResponse toResponse(Comment comment);
}
