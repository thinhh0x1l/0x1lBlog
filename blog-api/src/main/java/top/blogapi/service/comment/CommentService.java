package top.blogapi.service.comment;

import top.blogapi.model.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment create(Comment comment);
    Comment update(Long id, String content);
    void softDelete(Long id);
    List<Comment> getRootByBlogId(Long blogId, int page, int size);
    List<Comment> getReplies(Long parentId);
    void approve(Long id);
    void reject(Long id);
    void flag(Long id);
    long countByBlogId(Long blogId);
    long countRootByBlogId(Long blogId);
}
