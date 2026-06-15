package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.BlogSeries;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface BlogSeriesRepository {

    @Insert("""
        INSERT INTO blog_series (name, description, cover_url, author_id, published, created_at, updated_at)
        VALUES (#{name}, #{description}, #{coverUrl}, #{authorId}, #{published}, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(BlogSeries series);

    @Select("SELECT * FROM blog_series WHERE id = #{id} AND deleted_at IS NULL")
    Optional<BlogSeries> findById(@Param("id") Long id);

    @Select("SELECT * FROM blog_series WHERE author_id = #{authorId} AND deleted_at IS NULL ORDER BY created_at DESC")
    List<BlogSeries> findByAuthorId(@Param("authorId") Long authorId);

    @Select("SELECT * FROM blog_series WHERE published = TRUE AND deleted_at IS NULL ORDER BY created_at DESC")
    List<BlogSeries> findAllPublished();

    @Update("""
        UPDATE blog_series SET name = #{name}, description = #{description}, cover_url = #{coverUrl},
        published = #{published}, updated_at = NOW() WHERE id = #{id}
    """)
    int update(BlogSeries series);

    @Update("UPDATE blog_series SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM blog_series WHERE author_id = #{authorId} AND deleted_at IS NULL")
    int countByAuthorId(@Param("authorId") Long authorId);
}
