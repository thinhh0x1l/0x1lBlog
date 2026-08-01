package top.blogapi.user.auth.domain.repository;

import top.blogapi.user.auth.domain.entity.OAuth2Account;

import java.util.List;
import java.util.Optional;

public interface OAuth2AccountRepository {

    Optional<OAuth2Account> findByProvider(String provider, String providerId);

    List<OAuth2Account> findByUserId(Long userId);

    List<OAuth2Account> findByEmail(String email);

    void insert(OAuth2Account account);
}
