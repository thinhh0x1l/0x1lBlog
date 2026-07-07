package top.blogapi.security.auth;

/**
 * Giao diện service cho thao tác JWT token, bao gồm tạo, xác thực
 * và trích xuất claims như userId và role.
 */
public interface JwtService {
    String generateAccessToken(Long userId, String role);
    String generateRefreshToken(Long userId);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);
    boolean validateToken(String token);
}
