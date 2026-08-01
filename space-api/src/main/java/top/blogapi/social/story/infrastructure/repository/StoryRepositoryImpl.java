package top.blogapi.social.story.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.story.domain.entity.Story;
import top.blogapi.social.story.domain.repository.StoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoryRepositoryImpl implements StoryRepository {

    private final StoryJpaRepository jpa;
    private final StoryMybatisMapper mybatis;

    @Override
    public Optional<Story> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Story> findActiveByUserId(Long userId) {
        return mybatis.findActiveByUserId(userId);
    }

    @Override
    public List<Story> findActiveFeed(int limit) {
        return mybatis.findActiveFeed(limit);
    }

    @Override
    public void insert(Story story) {
        jpa.save(story);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public void incrementViewCount(Long id) {
        jpa.incrementViewCount(id);
    }
}
