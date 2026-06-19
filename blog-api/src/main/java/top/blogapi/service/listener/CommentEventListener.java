package top.blogapi.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.Comment;
import top.blogapi.model.event.CommentCreatedEvent;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CommentRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @EventListener
    public void handleCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        blogRepository.incrementCommentCount(comment.getBlogId());
        if (comment.getParentId() != null) {
            commentRepository.refreshReplyCount(comment.getParentId());
        }
        log.info("Comment created: id={}, blogId={}", comment.getId(), comment.getBlogId());
    }
}
