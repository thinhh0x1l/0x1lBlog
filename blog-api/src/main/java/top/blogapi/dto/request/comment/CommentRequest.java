package top.blogapi.dto.request.comment;

import lombok.Data;

@Data
public class CommentRequest {
    private Long blogId;
    private Long parentId;
    private String content;
    private String guestName;
}
