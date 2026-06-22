package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.enums.ReactionType;
import top.blogapi.service.reaction.ReactionService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReactionOrchestrator {

    private final ReactionService reactionService;

    @Transactional
    public void react(Long userId, Long blogId, ReactionType type) {
        reactionService.react(userId, blogId, type);
    }

    @Transactional
    public void unreact(Long userId, Long blogId) {
        reactionService.unreact(userId, blogId);
    }

    public Map<String, Object> getSummary(Long blogId, Long userId) {
        Map<String, Object> result = new HashMap<>(reactionService.getSummary(blogId));
        if (userId != null) {
            result.put("userReaction", reactionService.getUserReaction(userId, blogId));
        }
        return result;
    }
}
