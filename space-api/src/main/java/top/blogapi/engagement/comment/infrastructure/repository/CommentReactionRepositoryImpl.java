package top.blogapi.engagement.comment.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.CommentReaction;
import top.blogapi.engagement.comment.domain.repository.CommentReactionRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentReactionRepositoryImpl implements CommentReactionRepository {

    private final CommentReactionJpaRepository jpa;

    @Override
    public Optional<CommentReaction> findByUserAndComment(Long userId, Long commentId) {
        return jpa.findByUserIdAndCommentId(userId, commentId);
    }

    @Override
    public void insert(CommentReaction reaction) {
        jpa.save(reaction);
    }

    @Override
    public void delete(Long userId, Long commentId) {
        jpa.deleteByUserIdAndCommentId(userId, commentId);
    }

    @Override
    public long countByCommentId(Long commentId) {
        return jpa.countByCommentId(commentId);
    }
}
