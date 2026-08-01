package top.blogapi.social.story.domain.repository;

import top.blogapi.social.story.domain.entity.StoryArchive;

import java.util.List;

public interface StoryArchiveRepository {

    StoryArchive findById(Long id);

    List<StoryArchive> findByUserId(Long userId, int limit, int offset);

    void insert(StoryArchive archive);
}
