package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.OAuth2Account;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code oauth2_accounts}. Liên kết tài khoản
 * OAuth2 bên ngoài với tài khoản người dùng nội bộ.
 */
@Mapper
public interface OAuth2AccountRepository {

    @Select("SELECT * FROM oauth2_accounts WHERE provider = #{provider} AND provider_id = #{providerId}")
    Optional<OAuth2Account> findByProvider(String provider, String providerId);

    @Select("SELECT * FROM oauth2_accounts WHERE user_id = #{userId}")
    java.util.List<OAuth2Account> findByUserId(Long userId);

    @Select("SELECT * FROM oauth2_accounts WHERE email = #{email}")
    java.util.List<OAuth2Account> findByEmail(String email);

    @Insert("""
        INSERT INTO oauth2_accounts (user_id, provider, provider_id, email, avatar_url, raw_attributes)
        VALUES (#{userId}, #{provider}, #{providerId}, #{email}, #{avatarUrl}, #{rawAttributes}::jsonb)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OAuth2Account account);
}
