package top.blogapi.gamification.quest.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.quest.domain.entity.Quest;

import java.util.List;

@Repository
public interface QuestJpaRepository extends JpaRepository<Quest, Long> {

    @Query(value = "SELECT * FROM quests WHERE is_active = TRUE", nativeQuery = true)
    List<Quest> findActiveQuests();

    @Query(value = "SELECT * FROM quests WHERE type = :type AND is_active = TRUE", nativeQuery = true)
    List<Quest> findByType(@Param("type") String type);
}
