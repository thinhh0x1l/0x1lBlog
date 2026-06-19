package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Bookmark;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookmarkRepository {

    @Select("SELECT * FROM bookmarks WHERE id = #{id}")
    Optional<Bookmark> findById(Long id);

    @Select("SELECT * FROM bookmarks WHERE user_id = #{userId} AND blog_id = #{blogId}")
    Optional<Bookmark> findByUserAndBlog(Long userId, Long blogId);

    @Select("SELECT * FROM bookmarks WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Bookmark> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM bookmarks WHERE user_id = #{userId} AND collection = #{collection} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Bookmark> findByUserIdAndCollection(@Param("userId") Long userId, @Param("collection") String collection, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("""
        INSERT INTO bookmarks (user_id, blog_id, collection, note, is_public)
        VALUES (#{userId}, #{blogId}, #{collection}, #{note}, #{isPublic})
        ON CONFLICT (user_id, blog_id) DO UPDATE SET collection = #{collection}, note = #{note}, is_public = #{isPublic}
    """)
    int upsert(Bookmark bookmark);

    @Delete("DELETE FROM bookmarks WHERE user_id = #{userId} AND blog_id = #{blogId}")
    int delete(Long userId, Long blogId);

    @Select("SELECT COUNT(*) FROM bookmarks WHERE user_id = #{userId}")
    long countByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM bookmarks WHERE blog_id = #{blogId}")
    long countByBlogId(Long blogId);
}
