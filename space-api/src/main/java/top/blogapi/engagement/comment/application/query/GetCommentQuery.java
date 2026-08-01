package top.blogapi.engagement.comment.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.engagement.comment.domain.entity.Comment;
import top.blogapi.engagement.comment.domain.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCommentQuery {

    private final CommentService commentService;

    public List<Comment> getRootByTarget(String targetType, Long targetId, int page, int size) {
        return commentService.getRootByTarget(targetType, targetId, page, size);
    }

    public long countRootByTarget(String targetType, Long targetId) {
        return commentService.countRootByTarget(targetType, targetId);
    }

    public List<Comment> getReplies(Long parentId) {
        return commentService.getReplies(parentId);
    }
}
