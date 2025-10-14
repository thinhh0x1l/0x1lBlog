package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Blog;

import java.util.List;

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
}
