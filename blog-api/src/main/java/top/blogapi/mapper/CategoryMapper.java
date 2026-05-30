package top.blogapi.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.model.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);
}
