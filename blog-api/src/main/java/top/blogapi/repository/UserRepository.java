package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("SELECT * FROM users WHERE id = #{id} AND deleted_at IS NULL")
    Optional<User> findById(Long id);

    @Select("SELECT * FROM users WHERE username = #{username} AND deleted_at IS NULL")
    Optional<User> findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email} AND deleted_at IS NULL")
    Optional<User> findByEmail(String email);

    @Select("SELECT * FROM users WHERE username = #{username} OR email = #{email} AND deleted_at IS NULL")
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Insert("""
        INSERT INTO users (username, email, password_hash, display_name, avatar_url,
                           bio, website, location, role, is_creator, status)
        VALUES (#{username}, #{email}, #{passwordHash}, #{displayName}, #{avatarUrl},
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

    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE username = #{username} AND deleted_at IS NULL)")
    boolean existsByUsername(String username);

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
}
