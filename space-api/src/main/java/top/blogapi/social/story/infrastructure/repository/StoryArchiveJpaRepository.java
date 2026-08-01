package top.blogapi.social.story.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.social.story.domain.entity.StoryArchive;

import java.util.List;

@Repository
public interface StoryArchiveJpaRepository extends JpaRepository<StoryArchive, Long> {

    List<StoryArchive> findByUserIdOrderByArchivedAtDesc(Long userId);
}
