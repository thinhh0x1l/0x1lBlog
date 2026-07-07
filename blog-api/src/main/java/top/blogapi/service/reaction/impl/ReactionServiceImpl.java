package top.blogapi.service.reaction.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.Reaction;
import top.blogapi.model.enums.ReactionType;
import top.blogapi.repository.ReactionRepository;
import top.blogapi.service.reaction.ReactionService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai ReactionService cung cấp chuyển đổi cảm xúc dạng upsert
 * và tổng hợp số lượng theo các loại cảm xúc định sẵn.
 */
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;

    @Override
    public Reaction react(String targetType, Long targetId, Long userId, ReactionType type) {
        Reaction reaction = new Reaction();
        reaction.setUserId(userId);
        reaction.setTargetType(targetType);
        reaction.setTargetId(targetId);
        reaction.setType(type.getValue());
        reactionRepository.upsert(reaction);
        return reaction;
    }

    @Override
    public void unreact(String targetType, Long targetId, Long userId) {
        reactionRepository.delete(userId, targetType, targetId);
    }

    @Override
    public Map<String, Integer> getSummary(String targetType, Long targetId) {
        Map<String, Integer> summary = new HashMap<>();
        for (ReactionType type : ReactionType.values()) {
            summary.put(type.getValue().toLowerCase(), reactionRepository.countByTargetAndType(targetType, targetId, type.getValue()));
        }
        return summary;
    }

    @Override
    public ReactionType getUserReaction(String targetType, Long targetId, Long userId) {
        String type = reactionRepository.findTypeByUserAndTarget(userId, targetType, targetId);
        return type != null ? ReactionType.valueOf(type) : null;
    }
}
