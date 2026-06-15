package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.RefreshToken;

import java.util.Optional;

@Repository
@Mapper
public interface RefreshTokenRepository {

    @Select("SELECT * FROM refresh_token WHERE token = #{token}")
    Optional<RefreshToken> findByToken(@Param("token") String token);

    @Select("SELECT * FROM refresh_token WHERE user_id = #{userId} AND revoked = FALSE ORDER BY created_at DESC LIMIT 1")
    Optional<RefreshToken> findActiveByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO refresh_token (user_id, token, expires_at, created_at) VALUES (#{userId}, #{token}, #{expiresAt}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(RefreshToken refreshToken);

    @Update("UPDATE refresh_token SET revoked = TRUE WHERE id = #{id}")
    int revoke(@Param("id") Long id);

    @Update("UPDATE refresh_token SET revoked = TRUE WHERE user_id = #{userId}")
    int revokeAllByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM refresh_token WHERE expires_at < NOW()")
    int deleteExpired();
}
