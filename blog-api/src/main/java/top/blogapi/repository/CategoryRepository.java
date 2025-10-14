package top.blogapi.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Category;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    @Select("SELECT c.id, c.name FROM category c ORDER BY id DESC ")
    List<Category> getCategoryList();

    @Insert("INSERT INTO category (name) VALUES (#{category.name})")
    @Options(useGeneratedKeys = true, keyProperty = "category.id")
    int saveCategory(@Param("category") Category category);

    @Select("SELECT c.id, c.name FROM category c WHERE c.id = #{id}")
    Category getCategoryById(Long id);
}
