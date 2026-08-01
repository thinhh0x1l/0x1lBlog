package top.blogapi.content.blog.infrastructure.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.blogapi.content.blog.domain.entity.Blog;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BlogMybatisMapper {

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.id = #{id} AND b.deleted_at IS NULL
    """)
    Optional<Blog> findById(@Param("id") Long id);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.slug = #{slug} AND b.deleted_at IS NULL
    """)
    Optional<Blog> findBySlug(@Param("slug") String slug);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.status = 'PUBLISHED' AND b.deleted_at IS NULL
        ORDER BY b.published_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Blog> findPublished(@Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.author_id = #{authorId} AND b.deleted_at IS NULL
        ORDER BY b.created_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Blog> findByAuthorId(@Param("authorId") Long authorId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.category_id = #{categoryId} AND b.status = 'PUBLISHED' AND b.visibility = 'PUBLIC' AND b.deleted_at IS NULL
        ORDER BY b.published_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Blog> findByCategoryId(@Param("categoryId") Long categoryId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.status = 'PUBLISHED' AND b.deleted_at IS NULL
        ORDER BY (b.views * 0.3 + b.like_count * 2 + b.comment_count * 3 + b.bookmark_count * 4 + b.share_count * 5) DESC
        LIMIT #{limit}
    """)
    List<Blog> findTrending(@Param("limit") int limit);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.status = 'PUBLISHED' AND b.is_recommend = TRUE AND b.deleted_at IS NULL
        ORDER BY b.published_at DESC LIMIT #{limit}
    """)
    List<Blog> findRecommended(@Param("limit") int limit);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.status = 'PUBLISHED' AND b.deleted_at IS NULL
        AND (b.title ILIKE '%' || #{keyword} || '%' OR b.content ILIKE '%' || #{keyword} || '%')
        ORDER BY b.published_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Blog> search(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.status = 'PUBLISHED' AND b.visibility = 'PUBLIC' AND b.deleted_at IS NULL
        ORDER BY RANDOM() LIMIT 1
    """)
    Optional<Blog> findRandomPublished();

    @Select("""
        SELECT b.*, u.display_name AS author_name, u.avatar_url AS author_avatar, c.name AS category_name
        FROM blogs b
        LEFT JOIN users u ON b.author_id = u.id
        LEFT JOIN categories c ON b.category_id = c.id
        WHERE b.category_id = #{categoryId} AND b.status = 'PUBLISHED' AND b.visibility = 'PUBLIC' AND b.deleted_at IS NULL
        ORDER BY RANDOM() LIMIT 1
    """)
    Optional<Blog> findRandomByCategoryId(@Param("categoryId") Long categoryId);
}
