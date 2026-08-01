package top.blogapi.social.status.domain.event;

import top.blogapi.shared.event.DomainEvent;

public class StatusCreatedEvent extends DomainEvent {

    private final Long statusId;
    private final Long userId;

    public StatusCreatedEvent(Long statusId, Long userId) {
        super("status.created");
        this.statusId = statusId;
        this.userId = userId;
    }

    public Long getStatusId() { return statusId; }
    public Long getUserId() { return userId; }
}
