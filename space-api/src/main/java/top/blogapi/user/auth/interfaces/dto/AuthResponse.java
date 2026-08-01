package top.blogapi.user.auth.interfaces.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UserResponse user
) {}
