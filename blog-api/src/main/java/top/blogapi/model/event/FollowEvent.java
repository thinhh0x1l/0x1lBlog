package top.blogapi.model.event;

import lombok.Value;

/** Sự kiện được kích hoạt khi người dùng theo dõi hoặc bỏ theo dõi. */
@Value
public class FollowEvent {
    Long followerId;
    Long followingId;
    String action;
}
