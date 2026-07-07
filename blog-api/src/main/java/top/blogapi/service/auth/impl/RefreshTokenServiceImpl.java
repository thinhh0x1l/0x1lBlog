package top.blogapi.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.RefreshToken;
import top.blogapi.repository.RefreshTokenRepository;
import top.blogapi.service.auth.RefreshTokenService;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void persistRefreshToken(String token, Long userId, String ipAddress) {
        RefreshToken entity = new RefreshToken();
        entity.setUserId(userId);
        entity.setTokenHash(DigestUtils.sha256Hex(token));
        entity.setDeviceInfo("web");
        entity.setIpAddress(ipAddress != null ? ipAddress : "0.0.0.0");
        entity.setExpiresAt(OffsetDateTime.now().plusDays(30));
        refreshTokenRepository.insert(entity);
        log.debug("Refresh token persisted for user {}", userId);
    }

    @Override
    public RefreshToken findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash).orElse(null);
    }

    @Override
    public void revoke(Long id) {
        refreshTokenRepository.revoke(id);
    }

    @Override
    public void revokeAllByUserId(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }
}
