package top.blogapi.user.auth.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.auth.domain.entity.RefreshToken;
import top.blogapi.user.auth.domain.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaAdapter;

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpaAdapter.findByTokenHash(tokenHash);
    }

    @Override
    public void insert(RefreshToken token) {
        if (token.getCreatedAt() == null) {
            token.setCreatedAt(Instant.now());
        }
        jpaAdapter.save(token);
    }

    @Override
    public void revoke(Long id) {
        jpaAdapter.revoke(id);
    }

    @Override
    public void revokeAllByUserId(Long userId) {
        jpaAdapter.revokeAllByUserId(userId);
    }

    @Override
    public void deleteExpired() {
        jpaAdapter.deleteExpired();
    }
}
