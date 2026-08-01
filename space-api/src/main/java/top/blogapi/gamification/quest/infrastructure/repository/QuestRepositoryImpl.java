package top.blogapi.gamification.quest.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.quest.domain.entity.Quest;
import top.blogapi.gamification.quest.domain.repository.QuestRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestRepositoryImpl implements QuestRepository {

    private final QuestJpaRepository jpa;

    @Override
    public List<Quest> findActiveQuests() {
        return jpa.findActiveQuests();
    }

    @Override
    public List<Quest> findByType(String type) {
        return jpa.findByType(type);
    }

    @Override
    public Optional<Quest> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Quest> findAll() {
        return jpa.findAll();
    }
}
