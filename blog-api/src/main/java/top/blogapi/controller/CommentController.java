package top.blogapi.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.comment.CommentEditReq;
import top.blogapi.dto.request.comment.SaveCommentReq;
import top.blogapi.dto.response.comment.CommentByBlogIdResponse;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.CommentOrchestrator;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CommentController {
    CommentOrchestrator commentOrchestrator;


    @GetMapping("/comment-tree")
    public Result<CommentByBlogIdResponse> commentTree(@RequestParam Integer page,
                                                      @RequestParam(defaultValue = "") Long blogId,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest request){


        if (!commentOrchestrator.judgeCommentEnabled(page, blogId)) {
            return Result.create(403, "Chức năng bình luận đã bị tắt");
        }
        return Result.ok("Yêu cầu thành công!",commentOrchestrator
                .listCommentByBlogId(pageNum, pageSize, blogId, page, request ));
    }

    @PostMapping("/comment")
    public Result<?> createComment (@RequestBody SaveCommentReq req,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        commentOrchestrator.saveComment(req,request, response);
        return Result.ok("Đã viết bình luận");
    }

    @PutMapping("/comment")
    public Result<?> editComment(@RequestBody CommentEditReq req){
        commentOrchestrator.editComment(req);

        return Result.ok("Chỉnh sửa bình luận thành công");
    }
}
