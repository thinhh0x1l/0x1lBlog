package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.BlogSeries;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code blog_series}. Hỗ trợ CRUD, xóa mềm và
 * làm mới bộ đếm bài viết/người theo dõi.
 */
@Mapper
public interface BlogSeriesRepository {

    @Select("SELECT * FROM blog_series WHERE id = #{id} AND deleted_at IS NULL")
    Optional<BlogSeries> findById(Long id);

    @Select("SELECT * FROM blog_series WHERE author_id = #{authorId} AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<BlogSeries> findByAuthorId(@Param("authorId") Long authorId, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("""
        INSERT INTO blog_series (name, description, cover_image, author_id, status, price)
        VALUES (#{name}, #{description}, #{coverImage}, #{authorId}, #{status}, #{price})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlogSeries series);

    @Update("""
        UPDATE blog_series SET name = #{name}, description = #{description}, cover_image = #{coverImage},
                               status = #{status}, price = #{price}, updated_at = NOW()
        WHERE id = #{id} AND deleted_at IS NULL
    """)
    int update(BlogSeries series);

    @Update("UPDATE blog_series SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Update("UPDATE blog_series SET post_count = (SELECT COUNT(*) FROM series_blogs WHERE series_id = #{seriesId}) WHERE id = #{seriesId}")
    int refreshPostCount(Long seriesId);

    @Update("UPDATE blog_series SET subscriber_count = (SELECT COUNT(*) FROM series_subscribers WHERE series_id = #{seriesId}) WHERE id = #{seriesId}")
    int refreshSubscriberCount(Long seriesId);
}
