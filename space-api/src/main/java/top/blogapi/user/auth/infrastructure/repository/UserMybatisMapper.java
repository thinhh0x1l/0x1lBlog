package top.blogapi.user.auth.infrastructure.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.user.core.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Mapper
public interface UserMybatisMapper {


    @Update("UPDATE users SET last_active_at = NOW() WHERE id = #{id}")
    void updateLastActive(@Param("id") Long id);

    @Update("UPDATE users SET blog_count = (SELECT COUNT(*) FROM blogs WHERE author_id = #{userId} AND deleted_at IS NULL) WHERE id = #{userId}")
    void refreshBlogCount(@Param("userId") Long userId);

    @Update("UPDATE users SET ${column} = ${column} + #{amount} WHERE id = #{id}")
    void addReputationRaw(@Param("id") Long id, @Param("column") String column, @Param("amount") long amount);

    @Update("UPDATE users SET checkin_streak = #{streak}, last_checkin_at = #{date} WHERE id = #{userId}")
    void updateCheckin(@Param("userId") Long userId, @Param("streak") int streak, @Param("date") LocalDate date);

    @Select("SELECT * FROM users WHERE status = 'ACTIVE' AND deleted_at IS NULL ORDER BY RANDOM() LIMIT 1")
    Optional<User> findRandomActive();

    @Update("UPDATE users SET coins = coins + #{amount} WHERE id = #{userId}")
    void addCoins(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET gems = gems + #{amount} WHERE id = #{userId}")
    void addGems(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET coins = coins - #{amount} WHERE id = #{userId} AND coins >= #{amount}")
    void deductCoins(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET gems = gems - #{amount} WHERE id = #{userId} AND gems >= #{amount}")
    void deductGems(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET game_mode = #{gameMode} WHERE id = #{userId}")
    void updateGameMode(@Param("userId") Long userId, @Param("gameMode") boolean gameMode);

    @Update("UPDATE users SET role = #{role}, updated_at = NOW() WHERE id = #{id} AND deleted_at IS NULL")
    void updateRole(@Param("id") Long id, @Param("role") String role);

    @Update("UPDATE users SET status = 'BANNED', updated_at = NOW() WHERE id = #{id} AND deleted_at IS NULL")
    void banUser(@Param("id") Long id);
}
