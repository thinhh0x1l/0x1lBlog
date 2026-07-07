package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.CommentReaction;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code comment_reactions}. Quản lý lượt thích
 * bình luận với thêm/xóa và truy vấn đếm.
 */
@Mapper
public interface CommentReactionRepository {

    @Select("SELECT * FROM comment_reactions WHERE user_id = #{userId} AND comment_id = #{commentId}")
    Optional<CommentReaction> findByUserAndComment(Long userId, Long commentId);

    @Insert("INSERT INTO comment_reactions (user_id, comment_id) VALUES (#{userId}, #{commentId}) ON CONFLICT (user_id, comment_id) DO NOTHING")
    int insert(CommentReaction reaction);

    @Delete("DELETE FROM comment_reactions WHERE user_id = #{userId} AND comment_id = #{commentId}")
    int delete(Long userId, Long commentId);

    @Select("SELECT COUNT(*) FROM comment_reactions WHERE comment_id = #{commentId}")
    long countByCommentId(Long commentId);
}
