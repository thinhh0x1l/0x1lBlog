package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.enums.ReactionType;
import top.blogapi.service.reaction.ReactionService;

import java.util.HashMap;
import java.util.Map;

/**
 * Orchestrates reaction toggle, removal, and summary retrieval for any target type.
 */
@Component
@RequiredArgsConstructor
public class ReactionOrchestrator {

    private final ReactionService reactionService;

    @Transactional
    public void react(String targetType, Long targetId, Long userId, String type) {
        reactionService.react(targetType, targetId, userId, ReactionType.valueOf(type));
    }

    @Transactional
    public void unreact(String targetType, Long targetId, Long userId) {
        reactionService.unreact(targetType, targetId, userId);
    }

    public Map<String, Object> getSummary(String targetType, Long targetId, Long userId) {
        Map<String, Object> result = new HashMap<>(reactionService.getSummary(targetType, targetId));
        if (userId != null) {
            ReactionType userReaction = reactionService.getUserReaction(targetType, targetId, userId);
            result.put("userReaction", userReaction != null ? userReaction.getValue() : null);
        }
        return result;
    }
}
