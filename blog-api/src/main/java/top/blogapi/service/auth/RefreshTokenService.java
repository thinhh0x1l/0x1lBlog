package top.blogapi.service.auth;

import top.blogapi.model.entity.RefreshToken;

public interface RefreshTokenService {
    void persistRefreshToken(String token, Long userId, String ipAddress);
    RefreshToken findByTokenHash(String tokenHash);
    void revoke(Long id);
    void revokeAllByUserId(Long userId);
}
