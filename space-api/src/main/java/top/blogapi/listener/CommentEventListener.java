package top.blogapi.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.blogapi.engagement.comment.domain.event.CommentCreatedEvent;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.engagement.comment.domain.repository.CommentRepository;

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
        if ("BLOG".equals(event.getTargetType())) {
            blogRepository.incrementCommentCount(event.getTargetId());
        }
        if (event.getParentId() != null) {
            commentRepository.refreshReplyCount(event.getParentId());
        }
        log.info("Comment created: id={}, targetType={}, targetId={}", event.getCommentId(), event.getTargetType(), event.getTargetId());
    }
}
