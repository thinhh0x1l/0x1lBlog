package top.blogapi.model.event;

import lombok.Value;

@Value
public class UserRegisteredEvent {
    Long userId;
    String username;
    String email;
}
