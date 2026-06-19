package top.blogapi.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private UserResponse user;
}
