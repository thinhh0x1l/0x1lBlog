package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.dto.internal.BlogTagsInfoInternal;
import top.blogapi.model.entity.Tag;
import top.blogapi.dto.internal.TagBlogCount;

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
                STRING_AGG(t.name, '||') AS allTagNames,
                STRING_AGG(t.color, '||') AS allTagColors
            FROM tag t
            JOIN blog_tag bt
            ON t.id = bt.tag_id
            GROUP BY bt.blog_id
            HAVING COUNT(CASE WHEN t.name = #{tagName} THEN 1 END) > 0
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
    })
    List<BlogTagsInfoInternal> getBlogInfoListByTagNameAndIsPublished(String tagName);

    @Select("""
        SELECT t.id, t.name, t.color,
               SUM(CASE WHEN bt.blog_id IS NOT NULL THEN 1 ELSE 0 END)AS value
        FROM tag t
        LEFT JOIN blog_tag bt
        ON t.id = bt.tag_id
        GROUP BY t.id
""")
    List<TagBlogCount> getListTagBlogCount();
}
