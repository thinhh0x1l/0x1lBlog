package top.blogapi.social.story.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.story.domain.entity.StoryArchive;
import top.blogapi.social.story.domain.repository.StoryArchiveRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoryArchiveRepositoryImpl implements StoryArchiveRepository {

    private final StoryArchiveJpaRepository jpa;

    @Override
    public StoryArchive findById(Long id) {
        return jpa.findById(id).orElse(null);
    }

    @Override
    public List<StoryArchive> findByUserId(Long userId, int limit, int offset) {
        return jpa.findByUserIdOrderByArchivedAtDesc(userId)
                .stream()
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    public void insert(StoryArchive archive) {
        jpa.save(archive);
    }
}
