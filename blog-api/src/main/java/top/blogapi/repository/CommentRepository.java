package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Comment;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code comments}. Hỗ trợ CRUD, truy vấn trả lời
 * lồng nhau, quản lý trạng thái và làm mới bộ đếm phản hồi.
 */
@Mapper
public interface CommentRepository {

    @Select("""
        SELECT c.*, u.display_name AS author_name, u.avatar_url AS author_avatar
        FROM comments c
        LEFT JOIN users u ON c.user_id = u.id
        WHERE c.id = #{id} AND c.deleted_at IS NULL
    """)
    Optional<Comment> findById(Long id);

    @Select("""
        SELECT c.*, u.display_name AS author_name, u.avatar_url AS author_avatar
        FROM comments c
        LEFT JOIN users u ON c.user_id = u.id
        WHERE c.target_type = #{targetType} AND c.target_id = #{targetId}
        AND c.parent_id IS NULL AND c.status = 'APPROVED' AND c.deleted_at IS NULL
        ORDER BY c.created_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Comment> findRootByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT c.*, u.display_name AS author_name, u.avatar_url AS author_avatar
        FROM comments c
        LEFT JOIN users u ON c.user_id = u.id
        WHERE c.parent_id = #{parentId}
        AND c.status = 'APPROVED' AND c.deleted_at IS NULL
        ORDER BY c.created_at ASC
    """)
    List<Comment> findReplies(Long parentId);

    @Select("SELECT COUNT(*) FROM comments WHERE target_type = #{targetType} AND target_id = #{targetId} AND parent_id IS NULL AND status = 'APPROVED' AND deleted_at IS NULL")
    long countRootByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Insert("""
        INSERT INTO comments (target_type, target_id, parent_id, user_id, content, status)
        VALUES (#{targetType}, #{targetId}, #{parentId}, #{userId}, #{content}, #{status})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Update("""
        UPDATE comments SET content = #{content}, updated_at = NOW()
        WHERE id = #{id} AND deleted_at IS NULL
    """)
    int update(Comment comment);

    @Update("UPDATE comments SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE comments SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Select("SELECT COUNT(*) FROM comments WHERE target_type = #{targetType} AND target_id = #{targetId} AND status = 'APPROVED' AND deleted_at IS NULL")
    long countByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Update("UPDATE comments SET reply_count = (SELECT COUNT(*) FROM comments WHERE parent_id = #{commentId} AND deleted_at IS NULL) WHERE id = #{commentId}")
    int refreshReplyCount(Long commentId);
}
