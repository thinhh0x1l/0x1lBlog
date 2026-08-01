package top.blogapi.engagement.comment.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.Comment;
import top.blogapi.engagement.comment.domain.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpa;
    private final CommentMybatisMapper mybatis;

    @Override
    public Optional<Comment> findById(Long id) {
        return mybatis.findById(id);
    }

    @Override
    public List<Comment> findRootByTarget(String targetType, Long targetId, int limit, int offset) {
        return mybatis.findRootByTarget(targetType, targetId, limit, offset);
    }

    @Override
    public List<Comment> findReplies(Long parentId) {
        return mybatis.findReplies(parentId);
    }

    @Override
    public long countRootByTarget(String targetType, Long targetId) {
        return mybatis.countRootByTarget(targetType, targetId);
    }

    @Override
    public void insert(Comment comment) {
        jpa.save(comment);
    }

    @Override
    public void update(Comment comment) {
        jpa.updateContent(comment.getId(), comment.getContent());
    }

    @Override
    public void updateStatus(Long id, String status) {
        jpa.updateStatus(id, status);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public long countByTarget(String targetType, Long targetId) {
        return mybatis.countByTarget(targetType, targetId);
    }

    @Override
    public void refreshReplyCount(Long commentId) {
        mybatis.refreshReplyCount(commentId);
    }
}
