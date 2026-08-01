package top.blogapi.engagement.comment.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.comment.domain.service.CommentService;
import top.blogapi.engagement.comment.interfaces.dto.CommentMapper;
import top.blogapi.engagement.comment.interfaces.dto.CommentResponse;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UpdateCommentCommand {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse execute(Long id, String content, Long userId) {
        var comment = commentService.findById(id);
        if (!comment.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền chỉnh sửa bình luận này");
        }
        var updated = commentService.update(id, content);
        return commentMapper.toResponse(updated);
    }
}
