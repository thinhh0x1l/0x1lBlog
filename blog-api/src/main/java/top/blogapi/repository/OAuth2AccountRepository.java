package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.OAuth2Account;

import java.util.Optional;

@Mapper
public interface OAuth2AccountRepository {

    @Select("SELECT * FROM oauth2_accounts WHERE provider = #{provider} AND provider_id = #{providerId}")
    Optional<OAuth2Account> findByProvider(String provider, String providerId);

    @Select("SELECT * FROM oauth2_accounts WHERE user_id = #{userId}")
    java.util.List<OAuth2Account> findByUserId(Long userId);

    @Insert("""
        INSERT INTO oauth2_accounts (user_id, provider, provider_id, avatar_url, raw_attributes)
        VALUES (#{userId}, #{provider}, #{providerId}, #{avatarUrl}, #{rawAttributes}::jsonb)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OAuth2Account account);
}
