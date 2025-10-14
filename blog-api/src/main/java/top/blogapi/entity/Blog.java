package top.blogapi.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog {
    Long id;
    String title; // Tiêu đề bài viết
    String content; // Nội dung bài viết
    String firstPicture; // Ảnh bìa bài viết
    String description; // Mô tả
    String flag; // Loại bài viết (nguyên gốc, sao chép, dịch thuật)
    boolean published; // Đã xuất bản hay bản nháp
    boolean recommend; // Bật/tắt đề xuất
    boolean appreciation; // Bật/tắt chức năng ủng hộ (donate)
    boolean shareStatement; // Bật/tắt tuyên bố bản quyền
    boolean commentEnabled; // Bật/tắt bình luận
    LocalDateTime createTime; // Thời gian tạo
    LocalDateTime updateTime; // Thời gian cập nhật
    Integer views; // Lượt xem
    Integer words; // Số lượng từ trong bài viết
    Integer readTime; // Thời gian đọc (phút)

    User user; // Tác giả bài viết (vì là blog cá nhân nên có thể bỏ, tạm thời giữ lại)
    Category category; // Thể loại bài viết
    List<Tag> tags = new ArrayList<>(); // Danh sách thẻ (tag) của bài viết

}
