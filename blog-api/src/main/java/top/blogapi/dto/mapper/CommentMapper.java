package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.CommentResponse;
import top.blogapi.model.entity.Comment;

/**
 * Mapper MapStruct để chuyển đổi entity Comment sang CommentResponse DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);
}
