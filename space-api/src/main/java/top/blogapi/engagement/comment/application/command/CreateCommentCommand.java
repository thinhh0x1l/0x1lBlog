package top.blogapi.engagement.comment.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.comment.domain.entity.Comment;
import top.blogapi.engagement.comment.domain.event.CommentCreatedEvent;
import top.blogapi.engagement.comment.domain.service.CommentService;
import top.blogapi.engagement.comment.interfaces.dto.CommentMapper;
import top.blogapi.engagement.comment.interfaces.dto.CommentRequest;
import top.blogapi.engagement.comment.interfaces.dto.CommentResponse;

@Service
@RequiredArgsConstructor
public class CreateCommentCommand {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse execute(CommentRequest request, Long userId) {
        Comment comment = new Comment();
        comment.setTargetType(request.targetType());
        comment.setTargetId(request.targetId());
        comment.setParentId(request.parentId());
        comment.setUserId(userId);
        comment.setContent(request.content());
        comment.setStatus("APPROVED");
        comment = commentService.create(comment);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment.getId(), comment.getTargetType(), comment.getTargetId(), comment.getParentId()));

        return commentMapper.toResponse(comment);
    }
}
