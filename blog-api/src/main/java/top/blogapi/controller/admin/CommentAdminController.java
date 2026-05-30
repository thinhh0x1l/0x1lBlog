package top.blogapi.controller.admin;


import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.comment.CommentQueryRequest;
import top.blogapi.dto.request.comment.CommentUpdateRequest;
import top.blogapi.model.entity.Comment;
import top.blogapi.service.impl.orchestration.CommentOrchestrator;
import top.blogapi.dto.response._common.Result;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class CommentAdminController {
    CommentOrchestrator commentOrchestrator;

    @GetMapping("/comments")
    public Result<?> getComments(@ModelAttribute CommentQueryRequest request) {
        PageInfo<Comment> pageInfo =  commentOrchestrator.getListByPageAndParentCommentId(request);
        return Result.ok("Ok!",pageInfo);
    }

    @PutMapping("/comment/published/{id}")
    public Result<?> updatePublishedComment(@PathVariable Long id,@RequestParam boolean published) {
        return Result.ok(commentOrchestrator.updateCommentPublishedById(id, published));
    }

    @PutMapping("/comment/notice/{id}")
    public Result<?> updateNoticeComment(@PathVariable Long id,@RequestParam boolean notice) {
        return Result.ok(commentOrchestrator.updateCommentNoticeById(id, notice));
    }

    @PutMapping("/comment")
    public Result<?> updateComment(@RequestBody CommentUpdateRequest request) {
        return Result.ok(commentOrchestrator.updateComment(request));
    }

    @DeleteMapping("/comment/{id}")
    public Result<?> deleteComment(@PathVariable Long id) {
        return Result.ok(commentOrchestrator.deleteCommentById(id));
    }

}
