package top.blogapi.dto.response;

import lombok.Data;

/**
 * DTO phản hồi chứa token xác thực và thông tin người dùng.
 */
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private UserResponse user;
}
