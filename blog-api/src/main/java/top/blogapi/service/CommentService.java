package top.blogapi.service;

import com.github.pagehelper.PageInfo;
import top.blogapi.dto.request.comment.CommentUpdateRequest;
import top.blogapi.dto.response.comment.CommentByBlogIdResponse;
import top.blogapi.model.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<Comment>getListByPageAndParentCommentId(Integer page, Long parentCommentId, Long blogId) throws Exception;

    void updateCommentPublishedById(Long id, boolean published);

    void updateCommentNoticeById(Long id, boolean notice);

    void deleteCommentById(Long id);

    void updateComment(CommentUpdateRequest request);

    PageInfo<CommentByBlogIdResponse.CommentNode> commentRootTrees (int pageNum, int pageSize, Long blogId, Integer page, Long guessId);

    Map<Long, List<CommentByBlogIdResponse.CommentNode>> commentChildTrees(List<Long> commentRootIds, Long guessId);

    Long saveComment(Comment comment);

    void editComment(Long id, String content);
}
