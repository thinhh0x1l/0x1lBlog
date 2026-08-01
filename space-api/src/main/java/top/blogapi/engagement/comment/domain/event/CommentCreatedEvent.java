package top.blogapi.engagement.comment.domain.event;

import top.blogapi.shared.event.DomainEvent;

public class CommentCreatedEvent extends DomainEvent {

    private final Long commentId;
    private final String targetType;
    private final Long targetId;
    private final Long parentId;

    public CommentCreatedEvent(Long commentId, String targetType, Long targetId, Long parentId) {
        super("comment.created");
        this.commentId = commentId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.parentId = parentId;
    }

    public Long getCommentId() { return commentId; }
    public String getTargetType() { return targetType; }
    public Long getTargetId() { return targetId; }
    public Long getParentId() { return parentId; }
}
