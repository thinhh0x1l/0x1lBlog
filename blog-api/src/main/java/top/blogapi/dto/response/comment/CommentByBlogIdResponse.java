package top.blogapi.dto.response.comment;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.model.entity.Guess;
import top.blogapi.model.vo.PageResult;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentByBlogIdResponse {
    PageResult<CommentNode> comments;

    public CommentByBlogIdResponse(PageInfo<CommentNode> comments){
        this.comments = PageResult.from(comments);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CommentNode{
        Long id;
        String nickname;
        String content;
        String avatar;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createTime;
        Boolean adminComment;
        String reply;
        String website;
        boolean editAble = false;
        boolean isEdited;
        Long threadRoot;
        List<CommentNode> replyComment;

        public CommentNode setEditAble(Long id1, Long prId){
            this.editAble = prId != null && id1 == prId;
            return this;
        }
    }

}
