package top.blogapi.gamification.quest.domain.repository;

import top.blogapi.gamification.quest.domain.entity.UserQuest;

import java.util.List;
import java.util.Optional;

public interface UserQuestRepository {

    List<UserQuest> findByUserId(Long userId);

    List<UserQuest> findByUserIdAndStatus(Long userId, String status);

    Optional<UserQuest> findActiveByUserIdAndQuestId(Long userId, Long questId);

    List<UserQuest> findActiveQuests(Long userId);

    Optional<UserQuest> findById(Long id);

    void save(UserQuest userQuest);

    void update(UserQuest userQuest);

    int countActiveByType(Long userId, String type);
}
