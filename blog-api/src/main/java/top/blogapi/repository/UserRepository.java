package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * MyBatis mapper cho bảng {@code users}. Truy cập dữ liệu người dùng cốt
 * lõi với CRUD, tra cứu xác thực, danh tiếng, tiền tệ và cập nhật chế độ game.
 */
@Mapper
public interface UserRepository {

    @Select("SELECT * FROM users WHERE id = #{id} AND deleted_at IS NULL")
    Optional<User> findById(Long id);

    @Select("SELECT * FROM users WHERE email = #{email} AND deleted_at IS NULL")
    Optional<User> findByEmail(String email);

    @Insert("""
        INSERT INTO users (email, password_hash, display_name, avatar_url,
                           bio, website, location, role, is_creator, status)
        VALUES (#{email}, #{passwordHash}, #{displayName}, #{avatarUrl},
                #{bio}, #{website}, #{location}, #{role}, #{isCreator}, #{status})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("""
        UPDATE users SET display_name = #{displayName}, bio = #{bio}, website = #{website},
                          location = #{location}, avatar_url = #{avatarUrl},
                          updated_at = NOW()
        WHERE id = #{id} AND deleted_at IS NULL
    """)
    int update(User user);

    @Update("UPDATE users SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email} AND deleted_at IS NULL)")
    boolean existsByEmail(String email);

    @Select("SELECT * FROM users WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<User> findAll(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM users WHERE deleted_at IS NULL")
    long count();

    @Update("UPDATE users SET last_active_at = NOW() WHERE id = #{id}")
    int updateLastActive(Long id);

    @Update("UPDATE users SET blog_count = (SELECT COUNT(*) FROM blogs WHERE author_id = #{userId} AND deleted_at IS NULL) WHERE id = #{userId}")
    int refreshBlogCount(Long userId);

    @Update("UPDATE users SET ${column} = ${column} + #{amount} WHERE id = #{id}")
    void addReputationRaw(@Param("id") Long id, @Param("column") String column, @Param("amount") long amount);

    default void addReputation(Long id, String column, long amount) {
        if (!REPUTATION_COLUMNS.contains(column)) {
            throw new IllegalArgumentException("Invalid reputation column: " + column);
        }
        addReputationRaw(id, column, amount);
    }

    Set<String> REPUTATION_COLUMNS = Set.of(
            "reputation_writing", "reputation_community",
            "reputation_creativity", "reputation_influence"
    );

    @Update("UPDATE users SET checkin_streak = #{streak}, last_checkin_at = #{date} WHERE id = #{userId}")
    int updateCheckin(@Param("userId") Long userId, @Param("streak") int streak, @Param("date") LocalDate date);

    @Select("SELECT * FROM users WHERE status = 'ACTIVE' AND deleted_at IS NULL ORDER BY RANDOM() LIMIT 1")
    Optional<User> findRandomActive();

    @Update("UPDATE users SET coins = coins + #{amount} WHERE id = #{userId}")
    int addCoins(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET gems = gems + #{amount} WHERE id = #{userId}")
    int addGems(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET coins = coins - #{amount} WHERE id = #{userId} AND coins >= #{amount}")
    int deductCoins(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET gems = gems - #{amount} WHERE id = #{userId} AND gems >= #{amount}")
    int deductGems(@Param("userId") Long userId, @Param("amount") long amount);

    @Update("UPDATE users SET game_mode = #{gameMode} WHERE id = #{userId}")
    int updateGameMode(@Param("userId") Long userId, @Param("gameMode") boolean gameMode);
}
