package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Tag;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BlogRepository {
    // Chỉ lấy thông tin cơ bản như trong XML
    @Select("<script> " +
            "SELECT b.id, b.title, b.is_recommend, b.is_published, b.create_time, b.update_time, "+
                "c.id as category_id, c.name as category_name " +
            "FROM blog b LEFT JOIN category c " +
            "ON b.category_id = c.id " +
            "<where>" +
                "<if test='query != null and query != \"\"'> " +
                    "b.title LIKE CONCAT('%', #{query}, '%') " +
                "</if> " +
                "<if test='categoryId != null'> " +
                    "AND b.category_id = #{categoryId} " +
                "</if> " +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "recommend", column = "is_recommend"), // vì boolean cần phải map
            @Result(property = "published", column = "is_published"), // vì boolean cần phải map
            @Result(property = "category.id", column = "category_id"), // vì Đối tượng lồng cần phải map
            @Result(property = "category.name", column = "category_name") // vì Đối tượng lồng cần phải map
    })
    List<Blog> getListByTitleOrCategory(@Param("query") String query, @Param("categoryId") Integer categoryId);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    int deleteBlogById(@Param("id") Long id);

    @Delete("DELETE FROM blog_tag WHERE blog_id = #{blogId} ")
    int deleteBlogTagByBlogId(@Param("blogId") Long blogId);

    @Insert("INSERT INTO blog (title, content, first_picture, description, flag, is_published, is_recommend, is_appreciation, " +
            "     is_share_statement, is_comment_enabled, create_time, update_time, views, words, read_time, category_id, user_id) " +
            "VALUES (#{title}, #{content}, #{firstPicture}, #{description}, #{flag}, #{published}, #{recommend}, #{appreciation}, " +
            "     #{shareStatement}, #{commentEnabled}, #{createTime}, #{updateTime}, #{views}, #{words}, #{readTime}, #{category.id}, #{user.id}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveBlog(Blog blog);

    @Insert("INSERT INTO blog_tag (blog_id, tag_id) VALUES (#{blogId},#{tagId})")
    int saveBlogTag(Long blogId, Long tagId);

    @Update("UPDATE blog SET is_published = #{published} WHERE id = #{id}")
    int updateBlogPublishedById(@Param("id") Long id, @Param("published") boolean published);

    @Update("UPDATE blog SET is_recommend = #{recommend} WHERE id = #{id}")
    int updateBlogRecommendById(@Param("id")Long id, @Param("recommend") boolean recommend);

    @Select("SELECT b.id, b.title, b.content, b.first_picture, b.description, b.flag, b.is_published, b.is_recommend, " +
            "b.is_appreciation, b.is_share_statement, b.is_comment_enabled, b.create_time, b.update_time, " +
            "b.views, b.words, b.read_time, " +
            "c.id as category_id, c.name as category_name " +
            "FROM blog b " +
            "JOIN category c ON b.category_id = c.id " +
            "WHERE b.id = #{id}")
    @Results({
            @Result(property = "published", column = "is_published"),
            @Result(property = "recommend", column = "is_recommend"),
            @Result(property = "appreciation", column = "is_appreciation"),
            @Result(property = "shareStatement", column = "is_share_statement"),
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

    @Update("UPDATE blog SET title = #{title}, " +
            "content = #{content}, " +
            "first_picture = #{firstPicture}, " +
            "description = #{description}, " +
            "flag = #{flag}, " +
            "is_published = #{published}, " +
            "is_recommend = #{recommend}, " +
            "is_appreciation = #{appreciation}, " +
            "is_share_statement = #{shareStatement}, " +
            "is_comment_enabled = #{commentEnabled}, " +
            "create_time = #{createTime}, " +
            "update_time = #{updateTime}, " +
            "views = #{views}, " +
            "words = #{words}, " +
            "read_time = #{readTime}, " +
            "category_id = #{category.id} " +
            "WHERE id = #{id}")
    int updateBlog(Blog blog);

    @Select("SELECT COUNT(b.category_id) FROM blog b WHERE b.category_id = #{categoryId}")
    int countBlogByCategoryId(Long categoryId);
}
