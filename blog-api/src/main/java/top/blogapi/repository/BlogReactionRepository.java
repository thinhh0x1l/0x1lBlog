package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.BlogReaction;

import java.util.Optional;

@Repository
@Mapper
public interface BlogReactionRepository {

    @Insert("INSERT INTO blog_reaction (user_id, blog_id, type, created_at) VALUES (#{userId}, #{blogId}, #{type}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(BlogReaction reaction);

    @Update("UPDATE blog_reaction SET type = #{type} WHERE user_id = #{userId} AND blog_id = #{blogId}")
    int updateType(@Param("userId") Long userId, @Param("blogId") Long blogId, @Param("type") String type);

    @Delete("DELETE FROM blog_reaction WHERE user_id = #{userId} AND blog_id = #{blogId}")
    int delete(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("SELECT * FROM blog_reaction WHERE user_id = #{userId} AND blog_id = #{blogId} LIMIT 1")
    Optional<BlogReaction> findByUserAndBlog(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("SELECT type, COUNT(*) AS count FROM blog_reaction WHERE blog_id = #{blogId} GROUP BY type")
    java.util.List<ReactionCount> countByBlogId(@Param("blogId") Long blogId);

    interface ReactionCount {
        String getType();
        Long getCount();
    }
}
