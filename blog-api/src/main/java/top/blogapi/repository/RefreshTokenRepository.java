package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.RefreshToken;

import java.util.Optional;

@Mapper
public interface RefreshTokenRepository {

    @Select("SELECT * FROM refresh_tokens WHERE token_hash = #{tokenHash} AND revoked = FALSE AND expires_at > NOW()")
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Insert("""
        INSERT INTO refresh_tokens (user_id, token_hash, device_info, ip_address, expires_at)
        VALUES (#{userId}, #{tokenHash}, #{deviceInfo}, #{ipAddress}::inet, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RefreshToken token);

    @Update("UPDATE refresh_tokens SET revoked = TRUE WHERE id = #{id}")
    int revoke(Long id);

    @Update("UPDATE refresh_tokens SET revoked = TRUE WHERE user_id = #{userId}")
    int revokeAllByUserId(Long userId);

    @Delete("DELETE FROM refresh_tokens WHERE expires_at < NOW()")
    int deleteExpired();
}
