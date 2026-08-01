package top.blogapi.gamification.quest.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.quest.domain.entity.UserQuest;
import top.blogapi.gamification.quest.domain.service.QuestService;

@Service
@RequiredArgsConstructor
public class ClaimQuestRewardCommand {

    private final QuestService questService;

    @Transactional
    public UserQuest execute(Long userId, Long userQuestId) {
        return questService.claimReward(userId, userQuestId);
    }
}
