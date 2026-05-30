package top.blogapi.dto.request.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SaveCommentReq {
    String content;
    String nickname;
    String email;
    String website;
    boolean notice;
    Long blogId;
    Long parentCommentId;
    Integer page;
}
