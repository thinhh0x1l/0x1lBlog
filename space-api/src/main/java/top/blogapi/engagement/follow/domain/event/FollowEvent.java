package top.blogapi.engagement.follow.domain.event;

import top.blogapi.shared.event.DomainEvent;

public class FollowEvent extends DomainEvent {

    private final Long followerId;
    private final Long followingId;
    private final String action;

    public FollowEvent(Long followerId, Long followingId, String action) {
        super("follow." + action.toLowerCase());
        this.followerId = followerId;
        this.followingId = followingId;
        this.action = action;
    }

    public Long getFollowerId() { return followerId; }
    public Long getFollowingId() { return followingId; }
    public String getAction() { return action; }
}
