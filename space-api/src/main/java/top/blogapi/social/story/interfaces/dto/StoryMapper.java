package top.blogapi.social.story.interfaces.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.social.story.domain.entity.Story;
import top.blogapi.social.story.domain.entity.StoryArchive;

@Mapper(componentModel = "spring")
public interface StoryMapper {
    StoryResponse toResponse(Story story);

    @Mapping(target = "visibility", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "userId", source = "userId")
    StoryResponse toArchiveResponse(StoryArchive archive);
}
