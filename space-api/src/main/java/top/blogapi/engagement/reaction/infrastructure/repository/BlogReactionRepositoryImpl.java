package top.blogapi.engagement.reaction.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.reaction.domain.entity.BlogReaction;
import top.blogapi.engagement.reaction.domain.repository.BlogReactionRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogReactionRepositoryImpl implements BlogReactionRepository {

    private final BlogReactionJpaRepository jpa;

    @Override
    public Optional<BlogReaction> findByUserAndBlog(Long userId, Long blogId) {
        return jpa.findByUserIdAndBlogId(userId, blogId);
    }

    @Override
    public void upsert(BlogReaction reaction) {
        jpa.upsert(reaction.getUserId(), reaction.getBlogId(), reaction.getType());
    }

    @Override
    public void delete(Long userId, Long blogId) {
        jpa.deleteByUserIdAndBlogId(userId, blogId);
    }

    @Override
    public int countByBlogAndType(Long blogId, String type) {
        return jpa.countByBlogIdAndType(blogId, type);
    }

    @Override
    public String findTypeByUserAndBlog(Long userId, Long blogId) {
        return jpa.findTypeByUserIdAndBlogId(userId, blogId);
    }
}
