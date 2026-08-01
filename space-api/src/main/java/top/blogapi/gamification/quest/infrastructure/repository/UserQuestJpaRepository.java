package top.blogapi.gamification.quest.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.quest.domain.entity.UserQuest;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestJpaRepository extends JpaRepository<UserQuest, Long> {

    List<UserQuest> findByUserId(Long userId);

    List<UserQuest> findByUserIdAndStatus(Long userId, String status);

    Optional<UserQuest> findByUserIdAndQuestIdAndStatus(Long userId, Long questId, String status);

    @Query(value = "SELECT * FROM user_quests WHERE user_id = :userId AND status = 'IN_PROGRESS' AND expires_at > NOW()", nativeQuery = true)
    List<UserQuest> findActiveQuests(@Param("userId") Long userId);
}
