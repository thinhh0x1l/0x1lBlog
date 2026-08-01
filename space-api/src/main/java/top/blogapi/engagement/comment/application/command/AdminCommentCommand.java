package top.blogapi.engagement.comment.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.comment.domain.service.CommentService;

@Service
@RequiredArgsConstructor
public class AdminCommentCommand {

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
