package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Quest;
import top.blogapi.model.entity.UserQuest;
import top.blogapi.service.quest.QuestService;

import java.util.List;

/**
 * Orchestrates quest lifecycle: listing, assignment, progress tracking, reward claiming, and auto-assignment.
 */
@Component
@RequiredArgsConstructor
public class QuestOrchestrator {

    private final QuestService questService;

    public List<Quest> getActiveQuests() {
        return questService.getActiveQuests();
    }

    public List<UserQuest> getUserQuests(Long userId) {
        return questService.getUserQuests(userId);
    }

    @Transactional
    public UserQuest assignQuest(Long userId, Long questId) {
        return questService.assignQuest(userId, questId);
    }

    @Transactional
    public UserQuest updateProgress(Long userId, String action) {
        return questService.updateProgress(userId, action);
    }

    @Transactional
    public UserQuest claimReward(Long userId, Long userQuestId) {
        return questService.claimReward(userId, userQuestId);
    }

    @Transactional
    public List<UserQuest> autoAssignQuests(Long userId) {
        return questService.autoAssignQuests(userId);
    }
}
