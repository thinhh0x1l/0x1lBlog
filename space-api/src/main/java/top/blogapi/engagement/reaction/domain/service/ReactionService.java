package top.blogapi.engagement.reaction.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.engagement.reaction.domain.entity.Reaction;
import top.blogapi.engagement.reaction.domain.entity.enums.ReactionType;
import top.blogapi.engagement.reaction.domain.repository.ReactionRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public Reaction react(String targetType, Long targetId, Long userId, ReactionType type) {
        Reaction reaction = new Reaction();
        reaction.setUserId(userId);
        reaction.setTargetType(targetType);
        reaction.setTargetId(targetId);
        reaction.setType(type.getValue());
        reactionRepository.upsert(reaction);
        return reaction;
    }

    public void unreact(String targetType, Long targetId, Long userId) {
        reactionRepository.delete(userId, targetType, targetId);
    }

    public Map<String, Integer> getSummary(String targetType, Long targetId) {
        Map<String, Integer> summary = new HashMap<>();
        for (ReactionType type : ReactionType.values()) {
            summary.put(type.getValue().toLowerCase(), reactionRepository.countByTargetAndType(targetType, targetId, type.getValue()));
        }
        return summary;
    }

    public ReactionType getUserReaction(String targetType, Long targetId, Long userId) {
        String type = reactionRepository.findTypeByUserAndTarget(userId, targetType, targetId);
        return type != null ? ReactionType.valueOf(type) : null;
    }
}
