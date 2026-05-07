package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.BlogInfo;
import top.blogapi.model.vo.BlogTagsInfo;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface TagRepository {
    @Select("SELECT t.id, t.name, t.color FROM tag t ORDER BY id DESC ")
    List<Tag> getTagList();

    @Insert("INSERT INTO tag (name, color) VALUES (#{name}, #{color})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveTag(Tag tag);

    @Select("SELECT t.id, t.name, t.color FROM tag t WHERE id = #{id}")
    Optional<Tag> getTagById(@Param("id") Long id);

    @Select("SELECT t.id, t.name, t.color FROM tag t WHERE t.name = #{name}")
    Optional<Tag> getTagByName(String name);

    @Select("SELECT * FROM tag WHERE name = #{name}")
    Optional<Tag> tagExist(String name);

    @Delete("DELETE FROM tag WHERE id = #{tagId}")
    int deleteTagById(Long tagId);

    @Update("UPDATE tag SET name = #{name}, color = #{color} WHERE id = #{id}")
    int updateTag(String name, String color, Long id);

    @Select("SELECT t.id, t.name, t.color " +
            "FROM blog_tag bt " +
            "JOIN tag t ON bt.tag_id = t.id " +
            "WHERE bt.blog_id = #{blogId}")
    List<Tag> getTagListByBlogId(@Param("blogId") Long blogId);

    @Select("""
        WITH blog_tags AS (
            SELECT
                bt.blog_id,
                GROUP_CONCAT(t.name) AS allTagNames,
                GROUP_CONCAT(t.color) AS allTagColors
            FROM tag t
            JOIN blog_tag bt
            ON t.id = bt.tag_id
            GROUP BY bt.blog_id
            HAVING SUM(CASE WHEN t.name = #{tagName} THEN 1 ELSE 0 END) > 0
        )
        SELECT
            b.id,
            b.title,
            b.description,
            b.create_time,
            b.views,
            b.words,
            b.read_time,
            b.is_top,
            c.id AS category_id,
            c.name AS category_name,
            bt.allTagNames,
            bt.allTagColors
        FROM blog b
        JOIN blog_tags bt ON b.id = bt.blog_id
        JOIN category c ON c.id = b.category_id
        WHERE b.is_published
""")
    @Results({
            @Result(property = "top", column = "is_top"),
            @Result(property = "category.id", column = "category_id"),
            @Result(property = "category.name", column = "category_name")
    })
    List<BlogTagsInfo> getBlogInfoListByTagIdAndIsPublished(String tagName);
}
