package top.blogapi.engagement.comment.domain.repository;

import top.blogapi.engagement.comment.domain.entity.CommentReaction;

import java.util.Optional;

public interface CommentReactionRepository {

    Optional<CommentReaction> findByUserAndComment(Long userId, Long commentId);

    void insert(CommentReaction reaction);

    void delete(Long userId, Long commentId);

    long countByCommentId(Long commentId);
}
