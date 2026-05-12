package top.blogapi.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class CommentTree {
    Long id;
    String nickname; // Biệt danh (tên hiển thị của người bình luận)
    String content; // Nội dung bình luận
    String avatar; // Ảnh đại diện (đường dẫn ảnh)
    LocalDateTime createTime; // Thời gian bình luận
    Boolean adminComment; // Bình luận của quản trị viên (chủ blog)
    Long parentCommentId; // Bình luận cha (nếu là trả lời)
    String website;
    Long guessId;
    boolean isEdited;
    Long threadRoot;
    Long depth;
    String reply;
    List<CommentTree> replyComments; // Danh sách các bình luận trả lời bình luận này

    @Override
    public String toString() {
        return "CommentTree{" +
                "id=" + id +
                ", depth=" +depth +
                ", createTime=" + createTime +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", website='" + website + '\'' +
                ", parentCommentId=" + parentCommentId +
                ", threadRoot=" + threadRoot +
                ", reply=" + reply +
                ", replyComments=" + replyComments +
                '}';
    }
}
