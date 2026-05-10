package top.blogapi.dto.request._common;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String token;
    UserResponse user;

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserResponse {
        Long id;
        String username;
        String nickname;
        String avatar;
        String role;
    }
}
