package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.*;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BlogRepository {
    // Chỉ lấy thông tin cơ bản như trong XML
    @Select("""
            <script>
        SELECT b.id,
               b.title,
               b.is_recommend,
               b.is_published,
               b.create_time,
               b.update_time,
               b.is_top,
               c.id   AS category_id,
               c.name AS category_name
        FROM blog b
        LEFT JOIN category c
            ON b.category_id = c.id
        <where>
            <if test="query != null and query != ''">
                b.title LIKE CONCAT('%', #{query}, '%')
            </if>
            <if test="categoryId != null">
                AND b.category_id = #{categoryId}
            </if>
        </where>
    </script>
    """)
    @Results(id = "baseBlogMap", value = {
            @Result(property = "top", column = "is_top"),
            @Result(property = "recommend", column = "is_recommend"), // vì khác tên
            @Result(property = "published", column = "is_published"), // vì khác tên
            @Result(property = "category.id", column = "category_id"), // vì Đối tượng lồng cần phải map
            @Result(property = "category.name", column = "category_name") // vì Đối tượng lồng cần phải map
    })
    List<Blog> getListByTitleOrCategory(@Param("query") String query, @Param("categoryId") Integer categoryId);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    int deleteBlogById(@Param("id") Long id);

    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId} ")
    int deleteBlogTagByBlogId(@Param("blogId") Long blogId);

    @Insert("""
        INSERT INTO blog (
            title,
            content,
            description,
            is_published,
            is_recommend,
            is_appreciation,
            is_top,
            is_comment_enabled,
            create_time,
            update_time,
            music_id,
            views,
            words,
            read_time,
            category_id,
            user_id
        ) VALUES (
            #{title},
            #{content},
            #{description},
            #{published},
            #{recommend},
            #{appreciation},
            #{top},
            #{commentEnabled},
            #{createTime},
            #{updateTime},
            #{musicId},
            #{views},
            #{words},
            #{readTime},
            #{category.id},
            #{user.id}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveBlog(Blog blog);

    @Insert("""
            <script>
                INSERT INTO blog_tag (blog_id, tag_id) VALUES
                <foreach collection="tagIds" item="tagId" separator=",">
                    (#{blogId},#{tagId})
                </foreach>
            </script>

            """)
    int saveBlogTag(Long blogId, List<Long> tagIds);

    @Update("UPDATE blog SET is_top = #{top} WHERE id = #{id}")
    int updateBlogTopById(@Param("id") Long id, @Param("top") boolean top);

    @Update("UPDATE blog SET is_published = #{published} WHERE id = #{id}")
    int updateBlogPublishedById(@Param("id") Long id, @Param("published") boolean published);

    @Update("UPDATE blog SET is_recommend = #{recommend} WHERE id = #{id}")
    int updateBlogRecommendById(@Param("id")Long id, @Param("recommend") boolean recommend);

    @Select("""
            SELECT
                b.id,
                b.title,
                b.content,
                b.description,
                b.is_published,
                b.is_recommend,
                b.is_appreciation,
                b.is_comment_enabled,
                b.create_time,
                b.update_time,
                b.views,
                b.words,
                b.read_time,
                b.music_id,
                b.is_top,
                c.id as category_id,
                c.name as category_name
            FROM blog b
            JOIN category c ON b.category_id = c.id
            WHERE b.id = #{id}
    """)
    @Results({
            @Result(property = "published", column = "is_published"),
            @Result(property = "recommend", column = "is_recommend"),
            @Result(property = "top", column = "is_top"),
            @Result(property = "appreciation", column = "is_appreciation"),
            @Result(property = "commentEnabled", column = "is_comment_enabled"),
            @Result(property = "category.id", column = "category_id"),
            @Result(property = "category.name", column = "category_name"),
    })
    Optional<Blog> getBlogById(Long id);

    @Select("SELECT t.id, t.name, t.color " +
            "FROM blog_tag bt " +
            "JOIN tag t ON bt.tag_id = t.id " +
            "WHERE bt.blog_id = #{blogId}")
    List<Tag> findTagsByBlogId(@Param("blogId") Long blogId);

    @Update("""
        UPDATE blog
        SET title = #{title},
            content = #{content},
            description = #{description},
            is_published = #{published},
            is_recommend = #{recommend},
            is_top = #{top},
            is_appreciation = #{appreciation},
            is_comment_enabled = #{commentEnabled},
            create_time = #{createTime},
            music_id = #{musicId},
            update_time = #{updateTime},
            views = #{views},
            words = #{words},
            read_time = #{readTime},
            category_id = #{category.id}
        WHERE id = #{id}
    """)
    int updateBlog(Blog blog);

    @Select("SELECT COUNT(b.category_id) FROM blog b WHERE b.category_id = #{categoryId}")
    int countBlogByCategoryId(Long categoryId);

    @Select("SELECT COUNT(bt.tag_id) FROM blog_tag bt WHERE bt.tag_id = #{tagId} ")
    int countBlogByTagId(Long tagId);

    @Select("SELECT b.title, b.id FROM blog b ORDER BY create_time DESC ")
    List<BlogIdAndTitle> getIdAndTitleList();

    @Select("SELECT b.id, b.title, b.description, b.create_time, b.views, b.words, b.read_time," +
            "   b.is_top, c.id AS category_id, c.name AS category_name  " +
            "FROM blog b LEFT JOIN category c ON b.category_id = c.id " +
            "WHERE b.is_published = TRUE")
    @Results({
            @Result(property = "top", column = "is_top"),
            @Result(property = "category.id", column = "category_id"),
            @Result(property = "category.name", column = "category_name")
    })
    List<BlogInfo> getBlogInfoListByIsPublished();

    @Select("SELECT id, title FROM blog " +
            "WHERE is_published = TRUE AND is_recommend = TRUE " +
            "ORDER BY create_time DESC ")
    List<BlogIdAndTitle> getIdAndTitleListByIsPublishedAndIsRecommend();

    @Select("SELECT DISTINCT DATE_FORMAT(create_time,'%m/%Y') as day " +
            "FROM blog " +
            "WHERE is_published = TRUE "
    )
    List<String> getGroupYearMonthAndIsPublished();

    @Select("""
        <script>
            SELECT id,
                   title,
                   DATE_FORMAT(create_time, 'N%m') AS day,
                   DATE_FORMAT(create_time, '%m/%Y') AS yM
            FROM blog
            WHERE is_published = TRUE
              AND DATE_FORMAT(create_time, '%m/%Y') IN
            <foreach item="ym"
                     collection="yearMonths"
                     open="("
                     separator=","
                     close=")">
                #{ym}
            </foreach>
        </script>
    """)
    List<ArchiveBlog> getArchiveBlogListByYearMonthAndIsPublished(List<String> yearMonths);

    @Select("""
        SELECT b.id, b.title, b.content,  b.is_appreciation, b.music_id,
               b.is_comment_enabled, b.is_top, b.create_time, b.update_time, b.views, b.words ,
               b.read_time,c.id AS category_id, c.name AS category_name
        FROM blog AS b
        LEFT JOIN category AS c ON b.category_id = c.id
        WHERE b.id = #{id} AND b.is_published
    """)
    @Results({
            @Result(property = "category.name", column = "category_name"),
            @Result(property = "category.id", column = "category_id"),
            @Result(property = "appreciation", column = "is_appreciation"),
            @Result(property = "commentEnabled", column = "is_comment_enabled"),
            @Result(property = "top", column = "is_top"),
    })
    Optional<BlogDetail> getBlogWithCategory(Long id);

//    int getAmountOfCommentIs

    @Select("""
        SELECT is_comment_enabled from blog where id = #{blogId}
""")
    Boolean getCommentEnabledByBlogId(Long blogId);

    @Select("""
    SELECT b.id, b.title, c.id AS category_id, c.name AS category_name
    FROM blog AS b LEFT JOIN category AS c ON b.category_id=c.id
    WHERE b.is_published
    ORDER BY RAND()
    LIMIT #{limitNum}
""")
    @Results({
            @Result(property = "category.name", column = "category_name"),
            @Result(property = "category.id", column = "category_id"),
    })
    List<Blog> getRandomBlogListByLimitNumAndIsPublished(int limitNum);

    @Update("""
    UPDATE blog SET views = views + 1 WHERE id = #{blogId}
""")
    void updateView (Long blogId);

    @Select("""
    SELECT id, title, content FROM blog WHERE title LIKE CONCAT('%',#{search},'%') AND is_published = TRUE
""")
    List<SearchBlog> searchBlogs(String search);
}
