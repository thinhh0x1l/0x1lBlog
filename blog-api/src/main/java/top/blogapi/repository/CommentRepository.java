package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Comment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentRepository {

    @Select("SELECT * FROM comments WHERE id = #{id} AND deleted_at IS NULL")
    Optional<Comment> findById(Long id);

    @Select("""
        SELECT * FROM comments WHERE blog_id = #{blogId} AND parent_id IS NULL
        AND status = 'APPROVED' AND deleted_at IS NULL
        ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Comment> findRootByBlogId(@Param("blogId") Long blogId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT * FROM comments WHERE parent_id = #{parentId}
        AND status = 'APPROVED' AND deleted_at IS NULL
        ORDER BY created_at ASC
    """)
    List<Comment> findReplies(Long parentId);

    @Select("SELECT COUNT(*) FROM comments WHERE blog_id = #{blogId} AND parent_id IS NULL AND status = 'APPROVED' AND deleted_at IS NULL")
    long countRootByBlogId(Long blogId);

    @Insert("""
        INSERT INTO comments (blog_id, parent_id, user_id, guest_name, content, status)
        VALUES (#{blogId}, #{parentId}, #{userId}, #{guestName}, #{content}, #{status})
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

    @Select("SELECT COUNT(*) FROM comments WHERE blog_id = #{blogId} AND status = 'APPROVED' AND deleted_at IS NULL")
    long countByBlogId(Long blogId);

    @Update("UPDATE comments SET like_count = (SELECT COUNT(*) FROM comment_reactions WHERE comment_id = #{commentId}) WHERE id = #{commentId}")
    int refreshLikeCount(Long commentId);

    @Update("UPDATE comments SET reply_count = (SELECT COUNT(*) FROM comments WHERE parent_id = #{commentId} AND deleted_at IS NULL) WHERE id = #{commentId}")
    int refreshReplyCount(Long commentId);
}
