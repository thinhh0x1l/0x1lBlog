package top.blogapi.social.story.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.social.story.domain.entity.Story;
import top.blogapi.social.story.domain.entity.StoryArchive;
import top.blogapi.social.story.domain.service.StoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStoryQuery {

    private final StoryService storyService;

    public List<Story> getActiveStories(Long userId) {
        return storyService.getActiveByUserId(userId);
    }

    public List<Story> getActiveFeed() {
        return storyService.getActiveFeed(50);
    }

    public List<Story> getActiveStoriesByUser(Long targetUserId) {
        return storyService.getActiveByUserId(targetUserId);
    }

    public List<StoryArchive> getArchivedStories(Long userId, int page, int size) {
        return storyService.getArchivedByUserId(userId, page, size);
    }
}
