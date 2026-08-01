package top.blogapi.user.auth.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.auth.domain.entity.OAuth2Account;
import top.blogapi.user.auth.domain.repository.OAuth2AccountRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuth2AccountRepositoryImpl implements OAuth2AccountRepository {

    private final OAuth2AccountJpaRepository jpaAdapter;

    @Override
    public Optional<OAuth2Account> findByProvider(String provider, String providerId) {
        return jpaAdapter.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public List<OAuth2Account> findByUserId(Long userId) {
        return jpaAdapter.findByUserId(userId);
    }

    @Override
    public List<OAuth2Account> findByEmail(String email) {
        return jpaAdapter.findByEmail(email);
    }

    @Override
    public void insert(OAuth2Account account) {
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(Instant.now());
        }
        jpaAdapter.save(account);
    }
}
