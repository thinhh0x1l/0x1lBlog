package top.blogapi.service.auth;

public interface JwtService {
    String generateAccessToken(Long userId, String role);
    String generateRefreshToken(Long userId);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);
    boolean validateToken(String token);
}
