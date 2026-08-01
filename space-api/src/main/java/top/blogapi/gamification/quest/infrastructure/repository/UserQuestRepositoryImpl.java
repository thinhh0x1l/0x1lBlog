package top.blogapi.gamification.quest.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.quest.domain.entity.UserQuest;
import top.blogapi.gamification.quest.domain.repository.UserQuestRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQuestRepositoryImpl implements UserQuestRepository {

    private final UserQuestJpaRepository jpa;
    private final UserQuestMybatisMapper mybatis;

    @Override
    public List<UserQuest> findByUserId(Long userId) {
        return jpa.findByUserId(userId);
    }

    @Override
    public List<UserQuest> findByUserIdAndStatus(Long userId, String status) {
        return jpa.findByUserIdAndStatus(userId, status);
    }

    @Override
    public Optional<UserQuest> findActiveByUserIdAndQuestId(Long userId, Long questId) {
        return jpa.findByUserIdAndQuestIdAndStatus(userId, questId, "IN_PROGRESS");
    }

    @Override
    public List<UserQuest> findActiveQuests(Long userId) {
        return jpa.findActiveQuests(userId);
    }

    @Override
    public Optional<UserQuest> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public void save(UserQuest userQuest) {
        jpa.save(userQuest);
    }

    @Override
    public void update(UserQuest userQuest) {
        jpa.save(userQuest);
    }

    @Override
    public int countActiveByType(Long userId, String type) {
        return mybatis.countActiveByType(userId, type);
    }
}
