package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.CategoryResponse;
import top.blogapi.model.entity.Category;

/**
 * Mapper MapStruct để chuyển đổi entity Category sang CategoryResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);
}
