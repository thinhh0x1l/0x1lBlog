package top.blogapi.engagement.comment.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.engagement.comment.domain.entity.Comment;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.engagement.comment.domain.repository.CommentRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    }

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

    public Comment update(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setContent(content);
        commentRepository.update(comment);
        return comment;
    }

    public void softDelete(Long id) {
        commentRepository.softDelete(id);
    }

    public List<Comment> getRootByTarget(String targetType, Long targetId, int page, int size) {
        return commentRepository.findRootByTarget(targetType, targetId, size, page * size);
    }

    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findReplies(parentId);
    }

    public void approve(Long id) {
        commentRepository.updateStatus(id, "APPROVED");
    }

    public void reject(Long id) {
        commentRepository.updateStatus(id, "REJECTED");
    }

    public void flag(Long id) {
        commentRepository.updateStatus(id, "FLAGGED");
    }

    public long countByTarget(String targetType, Long targetId) {
        return commentRepository.countByTarget(targetType, targetId);
    }

    public long countRootByTarget(String targetType, Long targetId) {
        return commentRepository.countRootByTarget(targetType, targetId);
    }
}
