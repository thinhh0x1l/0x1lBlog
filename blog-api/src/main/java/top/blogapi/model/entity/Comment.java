package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.model.vo.BlogIdAndTitle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Comment {
    Long id;
    String nickname; // Biệt danh (tên hiển thị của người bình luận)
    String email; // Địa chỉ email
    String content; // Nội dung bình luận
    String avatar; // Ảnh đại diện (đường dẫn ảnh)
    String website;
    String ip; // Địa chỉ IP của người bình luận
    Boolean published; // Công khai hoặc đưa vào thùng rác
    Boolean notice; // Có nhận thông báo qua email hay không
    Boolean isEdited;
    LocalDateTime createTime; // Thời gian bình luận
    LocalDateTime updateAt; // Thời gian chỉnh sửa bình luận
    Long guessId;
    Boolean adminComment; // Bình luận của quản trị viên (chủ blog)m r
    Integer page; // 0: bài viết thông thường, 1: trang "Giới thiệu về tôi"
    BlogIdAndTitle blog; // Bài viết mà bình luận này thuộc về
    Long parentCommentId; // Bình luận cha (nếu là trả lời)
    List<Comment> replyComments = new ArrayList<>(); // Danh sách các bình luận trả lời bình luận này

    public Comment(){
        this.isEdited = false;
        this.updateAt = LocalDateTime.now();
    }
}
