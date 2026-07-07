package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.StoryArchive;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code story_archives}. Lưu trữ story đã hết
 * hạn để người dùng truy xuất sau.
 */
@Mapper
public interface StoryArchiveRepository {

    @Select("SELECT * FROM story_archives WHERE id = #{id}")
    StoryArchive findById(Long id);

    @Select("SELECT * FROM story_archives WHERE user_id = #{userId} ORDER BY archived_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<StoryArchive> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("""
        INSERT INTO story_archives (user_id, story_id, media_url, media_type, text_content, view_count)
        VALUES (#{userId}, #{storyId}, #{mediaUrl}, #{mediaType}, #{textContent}, #{viewCount})
        ON CONFLICT (story_id) DO NOTHING
    """)
    int insert(StoryArchive archive);
}
