package top.blogapi.social.story.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.story.domain.entity.Story;

@Repository
public interface StoryJpaRepository extends JpaRepository<Story, Long> {

    @Modifying
    @Query(value = "UPDATE stories SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE stories SET view_count = view_count + 1 WHERE id = :id", nativeQuery = true)
    void incrementViewCount(@Param("id") Long id);
}
