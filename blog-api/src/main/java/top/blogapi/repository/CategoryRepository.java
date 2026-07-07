package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code categories}. Cung cấp CRUD, lọc hiển thị
 * và làm mới bộ đếm blog.
 */
@Mapper
public interface CategoryRepository {

    @Select("SELECT * FROM categories WHERE id = #{id} AND deleted_at IS NULL")
    Optional<Category> findById(Long id);

    @Select("SELECT * FROM categories WHERE slug = #{slug} AND deleted_at IS NULL")
    Optional<Category> findBySlug(String slug);

    @Select("SELECT * FROM categories WHERE deleted_at IS NULL ORDER BY sort_order, name")
    List<Category> findAll();

    @Select("SELECT * FROM categories WHERE is_visible = TRUE AND deleted_at IS NULL ORDER BY sort_order, name")
    List<Category> findAllVisible();

    @Insert("""
        INSERT INTO categories (name, slug, description, icon, color, sort_order)
        VALUES (#{name}, #{slug}, #{description}, #{icon}, #{color}, #{sortOrder})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("""
        UPDATE categories SET name = #{name}, slug = #{slug}, description = #{description},
                               icon = #{icon}, color = #{color}, sort_order = #{sortOrder},
                               is_visible = #{isVisible}
        WHERE id = #{id}
    """)
    int update(Category category);

    @Update("UPDATE categories SET deleted_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    @Select("SELECT EXISTS(SELECT 1 FROM categories WHERE slug = #{slug} AND deleted_at IS NULL)")
    boolean existsBySlug(String slug);

    @Update("UPDATE categories SET blog_count = (SELECT COUNT(*) FROM blogs WHERE category_id = #{categoryId} AND status = 'PUBLISHED' AND deleted_at IS NULL) WHERE id = #{categoryId}")
    int refreshBlogCount(Long categoryId);
}
