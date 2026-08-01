package top.blogapi.social.story.domain.repository;

import top.blogapi.social.story.domain.entity.Story;

import java.util.List;
import java.util.Optional;

public interface StoryRepository {

    Optional<Story> findById(Long id);

    List<Story> findActiveByUserId(Long userId);

    List<Story> findActiveFeed(int limit);

    void insert(Story story);

    void softDelete(Long id);

    void incrementViewCount(Long id);
}
