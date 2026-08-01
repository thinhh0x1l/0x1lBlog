package top.blogapi.social.story.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.social.story.interfaces.dto.StoryRequest;
import top.blogapi.social.story.domain.entity.Story;
import top.blogapi.social.story.domain.service.StoryService;

@Service
@RequiredArgsConstructor
public class CreateStoryCommand {

    private final StoryService storyService;

    @Transactional
    public Story execute(StoryRequest request, Long userId) {
        Story story = new Story();
        story.setUserId(userId);
        story.setMediaUrl(request.mediaUrl());
        story.setMediaType(request.mediaType() != null ? request.mediaType() : "text");
        story.setTextContent(request.textContent());
        story.setVisibility(request.visibility() != null ? request.visibility() : "PUBLIC");
        story.setViewCount(0L);
        return storyService.create(story);
    }

    @Transactional
    public void deleteStory(Long id, Long userId) {
        storyService.softDelete(id, userId);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        storyService.incrementViewCount(id);
    }
}
