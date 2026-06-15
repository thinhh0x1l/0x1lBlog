package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Bookmark;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface BookmarkRepository {

    @Insert("INSERT INTO bookmark (user_id, blog_id, created_at) VALUES (#{userId}, #{blogId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Bookmark bookmark);

    @Delete("DELETE FROM bookmark WHERE user_id = #{userId} AND blog_id = #{blogId}")
    int delete(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("SELECT * FROM bookmark WHERE user_id = #{userId} AND blog_id = #{blogId} LIMIT 1")
    Optional<Bookmark> findByUserAndBlog(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("SELECT COUNT(*) > 0 FROM bookmark WHERE user_id = #{userId} AND blog_id = #{blogId}")
    boolean exists(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("SELECT b.*, bl.title AS blog_title FROM bookmark b JOIN blog bl ON b.blog_id = bl.id WHERE b.user_id = #{userId} ORDER BY b.created_at DESC")
    List<Bookmark> findByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM bookmark WHERE blog_id = #{blogId}")
    int countByBlogId(@Param("blogId") Long blogId);
}
