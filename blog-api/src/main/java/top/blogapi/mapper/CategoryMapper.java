package top.blogapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Category;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    @Select("SELECT c.id, c.name FROM category c ORDER BY id DESC ")
    List<Category> getCategoryList();
}
