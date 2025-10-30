package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Category;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryRepository {
    @Select("SELECT c.id, c.name FROM category c ORDER BY id DESC ")
    List<Category> getCategoryList();

    @Insert("INSERT INTO category (name) VALUES (#{category.name})")
    @Options(useGeneratedKeys = true, keyProperty = "category.id")
    int saveCategory(@Param("category") Category category);

    @Select("SELECT c.id, c.name FROM category c WHERE c.id = #{id}")
    Optional<Category> getCategoryById(Long id);

    @Select("SELECT c.id, c.name FROM category c WHERE c.name = #{name}")
    Optional<Category> getCategoryByName(String name);

    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteCategoryById(Long id);

    @Update("UPDATE category SET name = #{name} WHERE id = #{id}")
    int updateCategory(Category category);

}
