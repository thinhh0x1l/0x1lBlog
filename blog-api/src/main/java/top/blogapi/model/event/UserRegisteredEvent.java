package top.blogapi.model.event;

import lombok.Value;

/** Sự kiện được kích hoạt khi người dùng mới đăng ký nền tảng. */
@Value
public class UserRegisteredEvent {
    Long userId;
    String email;
}
