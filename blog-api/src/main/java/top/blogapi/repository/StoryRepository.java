package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Story;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code stories}. Quản lý bài story tạm thời với
 * truy vấn feed đang hoạt động và xóa mềm.
 */
@Mapper
public interface StoryRepository {

    @Select("SELECT * FROM stories WHERE id = #{id} AND deleted_at IS NULL")
    Optional<Story> findById(Long id);

    @Select("SELECT * FROM stories WHERE user_id = #{userId} AND deleted_at IS NULL AND expires_at > NOW() ORDER BY created_at DESC")
    List<Story> findActiveByUserId(Long userId);

    @Select("SELECT * FROM stories WHERE deleted_at IS NULL AND expires_at > NOW() ORDER BY created_at DESC LIMIT #{limit}")
    List<Story> findActiveFeed(@Param("limit") int limit);

    @Insert("""
        INSERT INTO stories (user_id, media_url, media_type, text_content, visibility, expires_at)
        VALUES (#{userId}, #{mediaUrl}, #{mediaType}, #{textContent}, #{visibility}, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Story story);

    @Update("UPDATE stories SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Update("UPDATE stories SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
}
