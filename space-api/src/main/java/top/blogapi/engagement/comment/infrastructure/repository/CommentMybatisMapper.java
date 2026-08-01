package top.blogapi.engagement.comment.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.blogapi.engagement.comment.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMybatisMapper {

    @Select("""
        SELECT c.*, u.display_name AS author_name, u.avatar_url AS author_avatar
        FROM comments c
        LEFT JOIN users u ON c.user_id = u.id
        WHERE c.id = #{id} AND c.deleted_at IS NULL
    """)
    Optional<Comment> findById(@Param("id") Long id);

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
    List<Comment> findReplies(@Param("parentId") Long parentId);

    @Select("SELECT COUNT(*) FROM comments WHERE target_type = #{targetType} AND target_id = #{targetId} AND parent_id IS NULL AND status = 'APPROVED' AND deleted_at IS NULL")
    long countRootByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Select("SELECT COUNT(*) FROM comments WHERE target_type = #{targetType} AND target_id = #{targetId} AND status = 'APPROVED' AND deleted_at IS NULL")
    long countByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Update("UPDATE comments SET reply_count = (SELECT COUNT(*) FROM comments WHERE parent_id = #{commentId} AND deleted_at IS NULL) WHERE id = #{commentId}")
    void refreshReplyCount(@Param("commentId") Long commentId);
}
