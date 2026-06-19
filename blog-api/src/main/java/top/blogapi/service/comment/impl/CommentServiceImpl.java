package top.blogapi.service.comment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Comment;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CommentRepository;
import top.blogapi.service.comment.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Override
    @Transactional
    public Comment create(Comment comment) {
        if (comment.getParentId() != null) {
            Comment parent = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            if (parent.getParentId() != null) {
                throw new AppException(ErrorCode.INVALID_INPUT, "Cannot reply to a reply (max 2 levels)");
            }
        }
        commentRepository.insert(comment);
        blogRepository.incrementCommentCount(comment.getBlogId());
        return comment;
    }

    @Override
    @Transactional
    public Comment update(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setContent(content);
        commentRepository.update(comment);
        return comment;
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        commentRepository.softDelete(id);
    }

    @Override
    public List<Comment> getRootByBlogId(Long blogId, int page, int size) {
        return commentRepository.findRootByBlogId(blogId, size, page * size);
    }

    @Override
    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findReplies(parentId);
    }

    @Override
    @Transactional
    public void approve(Long id) {
        commentRepository.updateStatus(id, "APPROVED");
    }

    @Override
    @Transactional
    public void reject(Long id) {
        commentRepository.updateStatus(id, "REJECTED");
    }

    @Override
    @Transactional
    public void flag(Long id) {
        commentRepository.updateStatus(id, "FLAGGED");
    }

    @Override
    public long countByBlogId(Long blogId) {
        return commentRepository.countByBlogId(blogId);
    }

    @Override
    public long countRootByBlogId(Long blogId) {
        return commentRepository.countRootByBlogId(blogId);
    }
}
