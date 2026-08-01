package top.blogapi.user.auth.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import top.blogapi.user.auth.domain.entity.RefreshToken;
import top.blogapi.user.auth.domain.repository.RefreshTokenRepository;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void persistRefreshToken(String token, Long userId, String ipAddress) {
        RefreshToken entity = new RefreshToken();
        entity.setUserId(userId);
        entity.setTokenHash(DigestUtils.sha256Hex(token));
        entity.setDeviceInfo("web");
        entity.setIpAddress(ipAddress != null ? ipAddress : "0.0.0.0");
        entity.setExpiresAt(Instant.now().plusSeconds(30 * 24 * 3600));
        refreshTokenRepository.insert(entity);
        log.debug("Refresh token persisted for user {}", userId);
    }

    public RefreshToken findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash).orElse(null);
    }

    public void revoke(Long id) {
        refreshTokenRepository.revoke(id);
    }

    public void revokeAllByUserId(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }
}
