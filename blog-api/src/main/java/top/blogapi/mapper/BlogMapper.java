package top.blogapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Blog;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {
    // Chỉ lấy thông tin cơ bản như trong XML
    @Select("SELECT b.id, b.title, b.is_recommend, b.is_published, b.create_time, b.update_time, " +
            "c.id as category_id, c.name as category_name " +
            "FROM blog b " +
            "JOIN category c " +
            "ON b.category_id = c.id ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "recommend", column = "is_recommend"),
            @Result(property = "published", column = "is_published"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "category.id", column = "category_id"),
            @Result(property = "category.name", column = "category_name")
    })
    List<Blog> getBlogList();
}
