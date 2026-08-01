package top.blogapi.gamification.quest.domain.repository;

import top.blogapi.gamification.quest.domain.entity.Quest;

import java.util.List;
import java.util.Optional;

public interface QuestRepository {

    List<Quest> findActiveQuests();

    List<Quest> findByType(String type);

    Optional<Quest> findById(Long id);

    List<Quest> findAll();
}
