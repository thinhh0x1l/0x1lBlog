package top.blogapi.content.category.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.content.category.domain.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);
}
