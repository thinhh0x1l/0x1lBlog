package top.blogapi.engagement.comment.domain.repository;

import top.blogapi.engagement.comment.domain.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long id);

    List<Comment> findRootByTarget(String targetType, Long targetId, int limit, int offset);

    List<Comment> findReplies(Long parentId);

    long countRootByTarget(String targetType, Long targetId);

    void insert(Comment comment);

    void update(Comment comment);

    void updateStatus(Long id, String status);

    void softDelete(Long id);

    long countByTarget(String targetType, Long targetId);

    void refreshReplyCount(Long commentId);
}
