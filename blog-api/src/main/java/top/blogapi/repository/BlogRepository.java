package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Blog;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BlogRepository {

    @Select("SELECT * FROM blogs WHERE id = #{id} AND deleted_at IS NULL")
    Optional<Blog> findById(Long id);

    @Select("SELECT * FROM blogs WHERE slug = #{slug} AND deleted_at IS NULL")
    Optional<Blog> findBySlug(String slug);

    @Select("SELECT * FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL ORDER BY published_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Blog> findPublished(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM blogs WHERE author_id = #{authorId} AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Blog> findByAuthorId(@Param("authorId") Long authorId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM blogs WHERE category_id = #{categoryId} AND status = 'PUBLISHED' AND visibility = 'PUBLIC' AND deleted_at IS NULL ORDER BY published_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Blog> findByCategoryId(@Param("categoryId") Long categoryId, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("""
        INSERT INTO blogs (author_id, category_id, title, slug, content, description,
                           cover_image, content_type, location_name, latitude, longitude,
                           status, visibility, price, is_top, is_recommend, allow_comments,
                           words, read_time)
        VALUES (#{authorId}, #{categoryId}, #{title}, #{slug}, #{content}, #{description},
                #{coverImage}, #{contentType}, #{locationName}, #{latitude}, #{longitude},
                #{status}, #{visibility}, #{price}, #{isTop}, #{isRecommend}, #{allowComments},
                #{words}, #{readTime})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Blog blog);

    @Update("""
        UPDATE blogs SET title = #{title}, content = #{content}, description = #{description},
                          cover_image = #{coverImage}, category_id = #{categoryId},
                          content_type = #{contentType}, allow_comments = #{allowComments},
                          updated_at = NOW()
        WHERE id = #{id} AND deleted_at IS NULL
    """)
    int update(Blog blog);

    @Update("""
        UPDATE blogs SET status = #{status}, published_at = CASE WHEN #{status} = 'PUBLISHED' AND published_at IS NULL THEN NOW() ELSE published_at END
        WHERE id = #{id} AND deleted_at IS NULL
    """)
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE blogs SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Select("SELECT COUNT(*) FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL")
    long countPublished();

    @Select("SELECT COUNT(*) FROM blogs WHERE author_id = #{authorId} AND deleted_at IS NULL")
    long countByAuthorId(Long authorId);

    @Select("""
        SELECT * FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL
        ORDER BY (views * 0.3 + like_count * 2 + comment_count * 3 + bookmark_count * 4 + share_count * 5) DESC
        LIMIT #{limit}
    """)
    List<Blog> findTrending(@Param("limit") int limit);

    @Select("""
        SELECT * FROM blogs WHERE status = 'PUBLISHED' AND is_recommend = TRUE AND deleted_at IS NULL
        ORDER BY published_at DESC LIMIT #{limit}
    """)
    List<Blog> findRecommended(@Param("limit") int limit);

    @Update("UPDATE blogs SET is_top = #{isTop} WHERE id = #{id}")
    int toggleTop(@Param("id") Long id, @Param("isTop") boolean isTop);

    @Update("UPDATE blogs SET is_recommend = #{isRecommend} WHERE id = #{id}")
    int toggleRecommend(@Param("id") Long id, @Param("isRecommend") boolean isRecommend);

    @Update("""
        UPDATE blogs SET views = views + 1 WHERE id = #{id}
    """)
    int incrementViews(Long id);

    @Update("UPDATE blogs SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(Long id);

    @Update("UPDATE blogs SET bookmark_count = bookmark_count + 1 WHERE id = #{id}")
    int incrementBookmarkCount(Long id);

    @Select("""
        SELECT * FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL
        AND (title ILIKE '%' || #{keyword} || '%' OR content ILIKE '%' || #{keyword} || '%')
        ORDER BY published_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Blog> search(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL AND (title ILIKE '%' || #{keyword} || '%' OR content ILIKE '%' || #{keyword} || '%')")
    long countSearch(String keyword);
}
