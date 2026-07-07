package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.request.blog.CreateBlogRequest;
import top.blogapi.dto.response.BlogResponse;
import top.blogapi.model.entity.Blog;

/**
 * Mapper MapStruct để chuyển đổi giữa entity Blog và DTO.
 */
@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "visibility", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "isTop", ignore = true)
    @Mapping(target = "isRecommend", ignore = true)
    @Mapping(target = "words", ignore = true)
    @Mapping(target = "readTime", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "loveCount", ignore = true)
    @Mapping(target = "hahaCount", ignore = true)
    @Mapping(target = "wowCount", ignore = true)
    @Mapping(target = "sadCount", ignore = true)
    @Mapping(target = "angryCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "bookmarkCount", ignore = true)
    @Mapping(target = "shareCount", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "lastCommentedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "locationName", source = "locationName")
    @Mapping(target = "allowComments", expression = "java(request.getAllowComments() != null ? request.getAllowComments() : true)")
    Blog toEntity(CreateBlogRequest request);

    BlogResponse toResponse(Blog blog);
}
