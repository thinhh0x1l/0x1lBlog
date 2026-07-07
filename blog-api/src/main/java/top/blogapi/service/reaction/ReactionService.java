package top.blogapi.service.reaction;

import top.blogapi.model.entity.Reaction;
import top.blogapi.model.enums.ReactionType;

import java.util.Map;

/**
 * Giao diện service cho cảm xúc nội dung, hỗ trợ cảm xúc dạng chuyển đổi
 * (thích/bỏ thích) với chuyển loại và tổng hợp số lượng.
 */
public interface ReactionService {
    Reaction react(String targetType, Long targetId, Long userId, ReactionType type);
    void unreact(String targetType, Long targetId, Long userId);
    Map<String, Integer> getSummary(String targetType, Long targetId);
    ReactionType getUserReaction(String targetType, Long targetId, Long userId);
}
