package top.blogapi.user.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.user.auth.domain.entity.OAuth2Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuth2AccountJpaRepository extends JpaRepository<OAuth2Account, Long> {

    Optional<OAuth2Account> findByProviderAndProviderId(String provider, String providerId);

    List<OAuth2Account> findByUserId(Long userId);

    List<OAuth2Account> findByEmail(String email);
}
