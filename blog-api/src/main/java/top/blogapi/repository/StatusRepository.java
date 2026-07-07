package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Status;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code statuses}. Quản lý bài trạng thái
 * micro-blog với hỗ trợ luồng, lọc hiển thị và xóa mềm.
 */
@Mapper
public interface StatusRepository {

    @Select("SELECT * FROM statuses WHERE id = #{id} AND deleted_at IS NULL")
    Optional<Status> findById(Long id);

    @Select("SELECT * FROM statuses WHERE user_id = #{userId} AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Status> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM statuses WHERE thread_id = #{threadId} AND deleted_at IS NULL ORDER BY part_order ASC")
    List<Status> findThreadParts(Long threadId);

    @Select("SELECT * FROM statuses WHERE visibility IN ('PUBLIC', 'FOLLOWERS') AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit}")
    List<Status> findFeed(@Param("limit") int limit);

    @Insert("""
        INSERT INTO statuses (user_id, thread_id, part_order, content, image_url, visibility)
        VALUES (#{userId}, #{threadId}, #{partOrder}, #{content}, #{imageUrl}, #{visibility})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Status status);

    @Update("UPDATE statuses SET content = #{content}, image_url = #{imageUrl}, updated_at = NOW() WHERE id = #{id} AND deleted_at IS NULL")
    int update(Status status);

    @Update("UPDATE statuses SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Select("SELECT COUNT(*) FROM statuses WHERE user_id = #{userId} AND created_at >= CURRENT_DATE AND deleted_at IS NULL")
    long countTodayByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM statuses WHERE user_id = #{userId} AND deleted_at IS NULL")
    long countByUserId(Long userId);
}
