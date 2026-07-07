package top.blogapi.service.story.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Story;
import top.blogapi.model.entity.StoryArchive;
import top.blogapi.repository.StoryArchiveRepository;
import top.blogapi.repository.StoryRepository;
import top.blogapi.service.story.StoryService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai StoryService áp dụng giới hạn 5 story/ngày, hết hạn sau 24 giờ,
 * tự động lưu trữ story hết hạn và theo dõi lượt xem.
 */
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final StoryArchiveRepository storyArchiveRepository;

    @Override
    public Story create(Story story) {
        long todayCount = countTodayByUserId(story.getUserId());
        if (todayCount >= 5) {
            throw new AppException(ErrorCode.STORY_DAILY_LIMIT);
        }
        story.setExpiresAt(OffsetDateTime.now().plusHours(24));
        storyRepository.insert(story);
        return story;
    }

    @Override
    public Story findById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));
    }

    @Override
    public List<Story> getActiveByUserId(Long userId) {
        return storyRepository.findActiveByUserId(userId);
    }

    @Override
    public List<Story> getActiveFeed(int limit) {
        return storyRepository.findActiveFeed(limit);
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Story story = findById(id);
        if (!story.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        storyRepository.softDelete(id);
    }

    @Override
    public void incrementViewCount(Long id) {
        storyRepository.incrementViewCount(id);
    }

    @Override
    public void archiveExpiredStories() {
        List<Story> expired = storyRepository.findActiveFeed(Integer.MAX_VALUE).stream()
                .filter(s -> s.getExpiresAt() != null && s.getExpiresAt().isBefore(OffsetDateTime.now()))
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

    @Override
    public long countTodayByUserId(Long userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        OffsetDateTime startOfDay = today.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        return storyRepository.findActiveByUserId(userId).stream()
                .filter(s -> s.getCreatedAt() != null
                        && !s.getCreatedAt().isBefore(startOfDay)
                        && s.getCreatedAt().isBefore(endOfDay))
                .count();
    }

    @Override
    public List<StoryArchive> getArchivedByUserId(Long userId, int page, int size) {
        return storyArchiveRepository.findByUserId(userId, size, page * size);
    }
}
