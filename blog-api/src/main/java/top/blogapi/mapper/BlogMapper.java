package top.blogapi.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Blog;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {
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
                "<if test='typeId != null'> " +
                    "AND b.category_id = #{typeId} " +
                "</if> " +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "recommend", column = "is_recommend"), // vì boolean cần phải map
            @Result(property = "published", column = "is_published"), // vì boolean cần phải map
            @Result(property = "category.id", column = "category_id"), // vì Đối tượng lồng cần phải map
            @Result(property = "category.name", column = "category_name") // vì Đối tượng lồng cần phải map
    })
    List<Blog> getListByTitleOrType(@Param("query") String query, @Param("typeId") Integer typeId);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    void deleteBlogById(@Param("id") Long id);
}
