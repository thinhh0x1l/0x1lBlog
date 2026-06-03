package top.blogapi.model.enums;

import lombok.Getter;

@Getter
public enum VisitBehavior {
    UNKNOWN("Không xác định", "Không xác định"),

    INDEX("Truy cập trang", "Trang chủ"),
    ARCHIVE("Truy cập trang", "Lưu trữ"),
    MOMENT("Truy cập trang", "Khoảnh khắc"),
    ABOUT("Truy cập trang", "Về tôi"),

    BLOG("Xem blog", ""),
    CATEGORY("Xem danh mục", ""),
    TAG("Xem thẻ", ""),
    SEARCH("Tìm kiếm blog", ""),
    MUSIC("Fetch MP3", "Nghe nhạc"),

    COMMENTS("Xem bình luận", "Danh sách bình luận"),
    POST_COMMENT("Viết bình luận", ""),
    LIKE_MOMENT("Thích khoảnh khắc", "");


    VisitBehavior(String behavior, String content) {
        this.behavior = behavior;
        this.content = content;
    }

    private final String behavior;
    private final String content;

}
