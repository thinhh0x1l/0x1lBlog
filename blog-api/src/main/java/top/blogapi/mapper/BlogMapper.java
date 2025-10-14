package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.entity.Blog;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(source = "category.name", target = "categoryName")
    BlogSummaryResponse toBlogSummaryResponse(Blog blog);
}
