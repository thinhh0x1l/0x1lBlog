package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.StoryResponse;
import top.blogapi.model.entity.Story;
import top.blogapi.model.entity.StoryArchive;

/**
 * Mapper MapStruct để chuyển đổi entity Story và StoryArchive sang StoryResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(target = "archivedAt", ignore = true)
    StoryResponse toResponse(Story story);

    @Mapping(target = "visibility", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    StoryResponse toResponse(StoryArchive archive);
}
