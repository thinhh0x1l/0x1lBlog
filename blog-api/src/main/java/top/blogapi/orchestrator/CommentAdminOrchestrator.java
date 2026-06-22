package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.service.comment.CommentService;

@Component
@RequiredArgsConstructor
public class CommentAdminOrchestrator {

    private final CommentService commentService;

    public void approve(Long id) {
        commentService.approve(id);
    }

    public void flag(Long id) {
        commentService.flag(id);
    }

    public void delete(Long id) {
        commentService.softDelete(id);
    }
}
