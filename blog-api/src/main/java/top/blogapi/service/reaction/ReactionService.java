package top.blogapi.service.reaction;

import top.blogapi.model.entity.BlogReaction;

import java.util.Map;

public interface ReactionService {
    BlogReaction react(Long userId, Long blogId, String type);
    void unreact(Long userId, Long blogId);
    Map<String, Integer> getSummary(Long blogId);
    String getUserReaction(Long userId, Long blogId);
}
