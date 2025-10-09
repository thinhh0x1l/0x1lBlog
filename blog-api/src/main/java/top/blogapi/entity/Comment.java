package top.blogapi.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    Long id;
    String nickname; // Biệt danh (tên hiển thị của người bình luận)
    String email; // Địa chỉ email
    String content; // Nội dung bình luận
    String avatar; // Ảnh đại diện (đường dẫn ảnh)
    Date createTime; // Thời gian bình luận
    String ip; // Địa chỉ IP của người bình luận
    boolean published; // Công khai hoặc đưa vào thùng rác
    boolean adminComment; // Bình luận của quản trị viên (chủ blog)
    Integer page; // 0: bài viết thông thường, 1: trang "Giới thiệu về tôi"
    boolean notice; // Có nhận thông báo qua email hay không

    Blog blog; // Bài viết mà bình luận này thuộc về
    Comment parentComment; // Bình luận cha (nếu là trả lời)
    List<Comment> replyComments = new ArrayList<>(); // Danh sách các bình luận trả lời bình luận này

}
