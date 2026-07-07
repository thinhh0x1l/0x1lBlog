package top.blogapi.orchestrator;

import lombok.Value;
import top.blogapi.dto.response.UserResponse;

/**
 * DTO chứa kết quả xác thực thành công (access token, refresh token và thông tin người dùng).
 */
@Value
public class AuthResult {
    String accessToken;
    String refreshToken;
    UserResponse user;
}
