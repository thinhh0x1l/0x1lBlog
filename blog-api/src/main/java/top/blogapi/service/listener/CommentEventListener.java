package top.blogapi.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.blogapi.model.entity.Comment;
import top.blogapi.model.event.CommentCreatedEvent;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CommentRepository;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * Lắng nghe sự kiện bình luận, tăng bộ đếm bình luận blog
 * và làm mới bộ đếm phản hồi cha khi có bình luận mới.
 */
public class CommentEventListener {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        if ("BLOG".equals(comment.getTargetType())) {
            blogRepository.incrementCommentCount(comment.getTargetId());
        }
        if (comment.getParentId() != null) {
            commentRepository.refreshReplyCount(comment.getParentId());
        }
        log.info("Comment created: id={}, targetType={}, targetId={}", comment.getId(), comment.getTargetType(), comment.getTargetId());
    }
}
