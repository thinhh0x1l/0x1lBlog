package top.blogapi.gamification.quest.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.quest.domain.entity.Quest;
import top.blogapi.gamification.quest.domain.entity.UserQuest;
import top.blogapi.gamification.quest.domain.service.QuestService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetQuestQuery {

    private final QuestService questService;

    public List<Quest> getActiveQuests() {
        return questService.getActiveQuests();
    }

    public List<UserQuest> getUserQuests(Long userId) {
        return questService.getUserQuests(userId);
    }

    @Transactional
    public List<UserQuest> autoAssignQuests(Long userId) {
        return questService.autoAssignQuests(userId);
    }
}
