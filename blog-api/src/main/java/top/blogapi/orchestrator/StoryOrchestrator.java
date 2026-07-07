package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.story.StoryRequest;
import top.blogapi.model.entity.Story;
import top.blogapi.model.entity.StoryArchive;
import top.blogapi.service.story.StoryService;

import java.util.List;

/**
 * Orchestrates story creation, active feed retrieval, view tracking, and archiving.
 */
@Component
@RequiredArgsConstructor
public class StoryOrchestrator {

    private final StoryService storyService;

    @Transactional
    public Story createStory(StoryRequest request, Long userId) {
        Story story = new Story();
        story.setUserId(userId);
        story.setMediaUrl(request.getMediaUrl());
        story.setMediaType(request.getMediaType() != null ? request.getMediaType() : "text");
        story.setTextContent(request.getTextContent());
        story.setVisibility(request.getVisibility() != null ? request.getVisibility() : "PUBLIC");
        story.setViewCount(0L);
        return storyService.create(story);
    }

    public List<Story> getActiveStories(Long userId) {
        return storyService.getActiveByUserId(userId);
    }

    public List<Story> getActiveFeed() {
        return storyService.getActiveFeed(50);
    }

    public List<Story> getActiveStoriesByUser(Long targetUserId) {
        return storyService.getActiveByUserId(targetUserId);
    }

    @Transactional
    public void deleteStory(Long id, Long userId) {
        storyService.softDelete(id, userId);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        storyService.incrementViewCount(id);
    }

    public List<StoryArchive> getArchivedStories(Long userId, int page, int size) {
        return storyService.getArchivedByUserId(userId, page, size);
    }
}
