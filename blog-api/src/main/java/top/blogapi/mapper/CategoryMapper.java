package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response.category.CategorySlug;
import top.blogapi.dto.response.tag.TagSlugs;
import top.blogapi.model.entity.Category;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.util.SlugUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);
}
