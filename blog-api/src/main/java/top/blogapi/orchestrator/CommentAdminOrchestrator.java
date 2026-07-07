package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.service.comment.CommentService;

/**
 * Điều phối các hành động kiểm duyệt bình luận: phê duyệt, từ chối, gắn cờ và xoá.
 */
@Component
@RequiredArgsConstructor
public class CommentAdminOrchestrator {

    private final CommentService commentService;

    @Transactional
    public void approve(Long id) {
        commentService.approve(id);
    }

    @Transactional
    public void reject(Long id) {
        commentService.reject(id);
    }

    @Transactional
    public void flag(Long id) {
        commentService.flag(id);
    }

    @Transactional
    public void delete(Long id) {
        commentService.softDelete(id);
    }
}
