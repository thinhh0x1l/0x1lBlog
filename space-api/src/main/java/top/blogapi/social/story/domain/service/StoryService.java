package top.blogapi.social.story.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.story.domain.entity.Story;
import top.blogapi.social.story.domain.entity.StoryArchive;
import top.blogapi.social.story.domain.repository.StoryArchiveRepository;
import top.blogapi.social.story.domain.repository.StoryRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final StoryArchiveRepository storyArchiveRepository;

    public Story create(Story story) {
        long todayCount = countTodayByUserId(story.getUserId());
        if (todayCount >= 5) {
            throw new AppException(ErrorCode.STORY_DAILY_LIMIT);
        }
        story.setExpiresAt(Instant.now().plusSeconds(24 * 3600));
        storyRepository.insert(story);
        return story;
    }

    public Story findById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
    }

    public List<Story> getActiveByUserId(Long userId) {
        return storyRepository.findActiveByUserId(userId);
    }

    public List<Story> getActiveFeed(int limit) {
        return storyRepository.findActiveFeed(limit);
    }

    public void softDelete(Long id, Long userId) {
        Story story = findById(id);
        if (!story.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        storyRepository.softDelete(id);
    }

    public void incrementViewCount(Long id) {
        storyRepository.incrementViewCount(id);
    }

    public void archiveExpiredStories() {
        List<Story> expired = storyRepository.findActiveFeed(Integer.MAX_VALUE).stream()
                .filter(s -> s.getExpiresAt() != null && s.getExpiresAt().isBefore(Instant.now()))
                .toList();
        for (Story story : expired) {
            StoryArchive archive = new StoryArchive();
            archive.setUserId(story.getUserId());
            archive.setStoryId(story.getId());
            archive.setMediaUrl(story.getMediaUrl());
            archive.setMediaType(story.getMediaType());
            archive.setTextContent(story.getTextContent());
            archive.setViewCount(story.getViewCount());
            storyArchiveRepository.insert(archive);
            storyRepository.softDelete(story.getId());
        }
    }

    public long countTodayByUserId(Long userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        return storyRepository.findActiveByUserId(userId).stream()
                .filter(s -> s.getCreatedAt() != null
                        && !s.getCreatedAt().isBefore(startOfDay)
                        && s.getCreatedAt().isBefore(endOfDay))
                .count();
    }

    public List<StoryArchive> getArchivedByUserId(Long userId, int page, int size) {
        return storyArchiveRepository.findByUserId(userId, size, page * size);
    }
}
