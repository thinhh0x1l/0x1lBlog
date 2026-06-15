package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.internal.BlogIdAndTitleInternal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Comment extends BaseEntity {
    String nickname;
    String email;
    String content;
    String avatar;
    String website;
    String ip;
    Boolean published;
    Boolean notice;
    Boolean isEdited;
    Long guessId;
    Boolean adminComment;
    Integer page;
    BlogIdAndTitleInternal blog;
    Long parentCommentId;
    List<Comment> replyComments = new ArrayList<>();

    public Comment(){
        this.isEdited = false;
    }
}
