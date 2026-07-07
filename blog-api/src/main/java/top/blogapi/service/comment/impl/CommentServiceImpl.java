package top.blogapi.service.comment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Comment;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CommentRepository;
import top.blogapi.service.comment.CommentService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai CommentService áp dụng giới hạn 2 cấp bình luận
 * và đồng bộ bộ đếm bình luận blog khi tạo mới.
 */
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Override
    public Comment create(Comment comment) {
        if (comment.getParentId() != null) {
            Comment parent = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            if (parent.getParentId() != null) {
                throw new AppException(ErrorCode.INVALID_INPUT, "Cannot reply to a reply (max 2 levels)");
            }
        }
        commentRepository.insert(comment);
        if ("BLOG".equals(comment.getTargetType())) {
            blogRepository.incrementCommentCount(comment.getTargetId());
        }
        return comment;
    }

    @Override
    public Comment update(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setContent(content);
        commentRepository.update(comment);
        return comment;
    }

    @Override
    public void softDelete(Long id) {
        commentRepository.softDelete(id);
    }

    @Override
    public List<Comment> getRootByTarget(String targetType, Long targetId, int page, int size) {
        return commentRepository.findRootByTarget(targetType, targetId, size, page * size);
    }

    @Override
    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findReplies(parentId);
    }

    @Override
    public void approve(Long id) {
        commentRepository.updateStatus(id, "APPROVED");
    }

    @Override
    public void reject(Long id) {
        commentRepository.updateStatus(id, "REJECTED");
    }

    @Override
    public void flag(Long id) {
        commentRepository.updateStatus(id, "FLAGGED");
    }

    @Override
    public long countByTarget(String targetType, Long targetId) {
        return commentRepository.countByTarget(targetType, targetId);
    }

    @Override
    public long countRootByTarget(String targetType, Long targetId) {
        return commentRepository.countRootByTarget(targetType, targetId);
    }
}
