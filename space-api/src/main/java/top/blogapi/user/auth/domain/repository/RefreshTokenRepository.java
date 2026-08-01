package top.blogapi.user.auth.domain.repository;

import top.blogapi.user.auth.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void insert(RefreshToken token);

    void revoke(Long id);

    void revokeAllByUserId(Long userId);

    void deleteExpired();
}
