package top.blogapi.model.event;

import lombok.Value;

@Value
public class FollowEvent {
    Long followerId;
    Long followingId;
    String action;
}
