package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.CommentMapper;
import top.blogapi.dto.request.comment.CommentRequest;
import top.blogapi.dto.response.CommentResponse;
import top.blogapi.model.entity.Comment;
import top.blogapi.model.event.CommentCreatedEvent;
import top.blogapi.service.comment.CommentService;

@Component
@RequiredArgsConstructor
public class CommentOrchestrator {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(CommentRequest request, Long userId) {
        Comment comment = new Comment();
        comment.setBlogId(request.getBlogId());
        comment.setParentId(request.getParentId());
        comment.setUserId(userId);
        comment.setGuestName(request.getGuestName());
        comment.setContent(request.getContent());
        comment.setStatus(userId != null ? "APPROVED" : "PENDING");
        comment = commentService.create(comment);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment));

        return commentMapper.toResponse(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long id, String content) {
        Comment comment = commentService.update(id, content);
        return commentMapper.toResponse(comment);
    }
}
