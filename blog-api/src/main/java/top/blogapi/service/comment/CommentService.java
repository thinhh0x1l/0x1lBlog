package top.blogapi.service.comment;

import top.blogapi.model.entity.Comment;

import java.util.List;

/**
 * Giao diện service quản lý bình luận, hỗ trợ bình luận phân cấp
 * (tối đa 2 cấp), kiểm duyệt và gắn cờ nội dung.
 */
public interface CommentService {
    Comment create(Comment comment);
    Comment update(Long id, String content);
    void softDelete(Long id);
    List<Comment> getRootByTarget(String targetType, Long targetId, int page, int size);
    List<Comment> getReplies(Long parentId);
    void approve(Long id);
    void reject(Long id);
    void flag(Long id);
    long countByTarget(String targetType, Long targetId);
    long countRootByTarget(String targetType, Long targetId);
}
