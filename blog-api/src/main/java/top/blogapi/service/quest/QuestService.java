package top.blogapi.service.quest;

import top.blogapi.model.entity.Quest;
import top.blogapi.model.entity.UserQuest;

import java.util.List;

/**
 * Giao diện service cho hệ thống nhiệm vụ, xử lý gán nhiệm vụ,
 * theo dõi tiến độ, nhận thưởng và tự động gán nhiệm vụ hàng ngày/tuần.
 */
public interface QuestService {
    List<Quest> getActiveQuests();
    List<UserQuest> getUserQuests(Long userId);
    UserQuest assignQuest(Long userId, Long questId);
    UserQuest updateProgress(Long userId, String action);
    UserQuest claimReward(Long userId, Long userQuestId);
    List<UserQuest> autoAssignQuests(Long userId);
}
