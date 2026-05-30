package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.internal.CommentTreeInternal;
import top.blogapi.dto.response.comment.CommentByBlogIdResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "replyComment", ignore = true)
    CommentByBlogIdResponse.CommentNode toCommentNode(CommentTreeInternal commentTreeInternal);

    List<CommentByBlogIdResponse.CommentNode> toCommentNodeList(List<CommentTreeInternal> commentTreeInternals);

}
