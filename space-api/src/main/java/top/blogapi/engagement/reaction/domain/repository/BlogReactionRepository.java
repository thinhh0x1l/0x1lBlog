package top.blogapi.engagement.reaction.domain.repository;

import top.blogapi.engagement.reaction.domain.entity.BlogReaction;

import java.util.Optional;

public interface BlogReactionRepository {

    Optional<BlogReaction> findByUserAndBlog(Long userId, Long blogId);

    void upsert(BlogReaction reaction);

    void delete(Long userId, Long blogId);

    int countByBlogAndType(Long blogId, String type);

    String findTypeByUserAndBlog(Long userId, Long blogId);
}
