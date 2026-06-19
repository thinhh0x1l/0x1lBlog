package top.blogapi.service.reaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.BlogReaction;
import top.blogapi.repository.BlogReactionRepository;
import top.blogapi.service.reaction.ReactionService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final BlogReactionRepository blogReactionRepository;

    @Override
    @Transactional
    public BlogReaction react(Long userId, Long blogId, String type) {
        BlogReaction reaction = new BlogReaction();
        reaction.setUserId(userId);
        reaction.setBlogId(blogId);
        reaction.setType(type);
        blogReactionRepository.upsert(reaction);
        return reaction;
    }

    @Override
    @Transactional
    public void unreact(Long userId, Long blogId) {
        blogReactionRepository.delete(userId, blogId);
    }

    @Override
    public Map<String, Integer> getSummary(Long blogId) {
        Map<String, Integer> summary = new HashMap<>();
        for (String type : new String[]{"LIKE", "LOVE", "HAHA", "WOW", "SAD", "ANGRY"}) {
            summary.put(type.toLowerCase(), blogReactionRepository.countByBlogAndType(blogId, type));
        }
        return summary;
    }

    @Override
    public String getUserReaction(Long userId, Long blogId) {
        return blogReactionRepository.findTypeByUserAndBlog(userId, blogId);
    }
}
