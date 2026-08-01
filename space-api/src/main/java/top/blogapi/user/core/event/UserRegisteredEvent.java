package top.blogapi.user.core.event;

import top.blogapi.shared.event.DomainEvent;

public class UserRegisteredEvent extends DomainEvent {

    private final Long userId;
    private final String email;

    public UserRegisteredEvent(Long userId, String email) {
        super("user.registered");
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
}
