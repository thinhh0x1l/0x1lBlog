package top.blogapi.engagement.comment.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.comment.domain.service.CommentService;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class DeleteCommentCommand {

    private final CommentService commentService;

    @Transactional
    public void execute(Long id, Long userId) {
        var comment = commentService.findById(id);
        if (!comment.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền xóa bình luận này");
        }
        commentService.softDelete(id);
    }
}
