package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Category;
import top.blogapi.model.vo.BlogTagsInfo;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryRepository {
    @Select("SELECT c.id, c.name FROM category c ORDER BY id DESC ")
    List<Category> getCategoryList();

    @Insert("INSERT INTO category (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveCategory(Category category);

    @Select("SELECT c.id, c.name FROM category c WHERE c.id = #{id}")
    Optional<Category> getCategoryById(Long id);

    @Select("SELECT c.id, c.name FROM category c WHERE c.name = #{name}")
    Optional<Category> getCategoryByName(String name);

    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteCategoryById(Long id);

    @Update("UPDATE category SET name = #{name} WHERE id = #{id}")
    int updateCategory(Category category);

    @Select("SELECT * FROM category WHERE name = #{categoryName}")
    Optional<Category> categoryExist(String categoryName);

    @Select("""
        WITH
            blog_base AS (
                SELECT
                    b.id,
                    b.title,
                    b.description,
                    b.create_time,
                    b.views,
                    b.words,
                    b.read_time,
                    b.is_top,
                    c.name AS category_name
                FROM category c
                JOIN blog b
                ON c.id = b.category_id
                WHERE c.name = #{categoryName}
                AND b.is_published
            ),
            blog_tags AS (
                SELECT
                    bt.blog_id,
                    GROUP_CONCAT(t.name SEPARATOR '||') AS allTagNames,
                    GROUP_CONCAT(t.color SEPARATOR '||') AS allTagColors
                FROM blog_tag bt
                JOIN tag t
                ON t.id = bt.tag_id
                GROUP BY bt.blog_id
            )
        SELECT
            b.*,
            bt.allTagNames,
            bt.allTagColors
        FROM blog_base b
        LEFT JOIN blog_tags bt ON bt.blog_id = b.id;
""")
    @Results({
            @Result(property = "top", column = "is_top"),
    })
    List<BlogTagsInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);

}
