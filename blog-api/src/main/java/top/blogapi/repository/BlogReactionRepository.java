package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.BlogReaction;

import java.util.Optional;

@Mapper
public interface BlogReactionRepository {

    @Select("SELECT * FROM blog_reactions WHERE user_id = #{userId} AND blog_id = #{blogId}")
    Optional<BlogReaction> findByUserAndBlog(Long userId, Long blogId);

    @Insert("""
        INSERT INTO blog_reactions (user_id, blog_id, type)
        VALUES (#{userId}, #{blogId}, #{type})
        ON CONFLICT (user_id, blog_id) DO UPDATE SET type = #{type}
    """)
    int upsert(BlogReaction reaction);

    @Delete("DELETE FROM blog_reactions WHERE user_id = #{userId} AND blog_id = #{blogId}")
    int delete(Long userId, Long blogId);

    @Select("SELECT COUNT(*) FROM blog_reactions WHERE blog_id = #{blogId} AND type = #{type}")
    int countByBlogAndType(@Param("blogId") Long blogId, @Param("type") String type);

    @Select("SELECT type FROM blog_reactions WHERE user_id = #{userId} AND blog_id = #{blogId}")
    String findTypeByUserAndBlog(Long userId, Long blogId);
}
