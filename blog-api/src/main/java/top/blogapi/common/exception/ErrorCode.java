package top.blogapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Danh sách mã lỗi tập trung với HTTP status kèm thông báo tiếng Việt.
 */
@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "Dữ liệu đầu vào không hợp lệ"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.BAD_REQUEST, "Sai tên đăng nhập hoặc mật khẩu"),
    CONTENT_REQUIRED("CONTENT_REQUIRED", HttpStatus.BAD_REQUEST, "Nội dung cần phải được điền"),
    MAX_DEPTH_REACHED("MAX_DEPTH_REACHED", HttpStatus.BAD_REQUEST, "Vượt quá độ sâu phản hồi tối đa (3 cấp)"),
    CANNOT_FOLLOW_SELF("CANNOT_FOLLOW_SELF", HttpStatus.BAD_REQUEST, "Không thể tự theo dõi bản thân"),
    ABOUT_UPDATE_PARTIAL("ABOUT_UPDATE_PARTIAL", HttpStatus.BAD_REQUEST, "Update thiếu row"),
    BLOG_MIN_CONTENT("BLOG_MIN_CONTENT", HttpStatus.BAD_REQUEST, "Nội dung bài viết phải có ít nhất 100 ký tự khi xuất bản"),
    BLOG_MAX_TAGS("BLOG_MAX_TAGS", HttpStatus.BAD_REQUEST, "Mỗi bài viết tối đa 5 thẻ"),
    BLOG_CATEGORY_REQUIRED("BLOG_CATEGORY_REQUIRED", HttpStatus.BAD_REQUEST, "Bài viết phải thuộc một danh mục"),
    BLOG_REPRINT_SOURCE_REQUIRED("BLOG_REPRINT_SOURCE_REQUIRED", HttpStatus.BAD_REQUEST, "Bài viết转载 phải có link nguồn gốc"),
    COMMENT_EDIT_WINDOW("COMMENT_EDIT_WINDOW", HttpStatus.BAD_REQUEST, "Chỉ được sửa trong vòng 10 phút sau khi tạo"),
    REACTION_SELF_NOT_ALLOWED("REACTION_SELF_NOT_ALLOWED", HttpStatus.BAD_REQUEST, "Không thể tương tác với nội dung của chính mình"),
    PLAYLIST_SONG_LIMIT("PLAYLIST_SONG_LIMIT", HttpStatus.BAD_REQUEST, "Playlist đã đạt tối đa 50 bài hát"),

    // 401 Unauthorized
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Bạn cần phải đăng nhập"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "Access token đã hết hạn"),
    TOKEN_REVOKED("TOKEN_REVOKED", HttpStatus.UNAUTHORIZED, "Refresh token đã bị thu hồi"),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ hoặc đã hết hạn"),
    OAUTH2_FAILED("OAUTH2_FAILED", HttpStatus.UNAUTHORIZED, "Xác thực OAuth2 thất bại"),

    // 403 Forbidden
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "Bạn không được phép truy cập"),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.FORBIDDEN, "Tài khoản bị khóa tạm thời"),
    ACCOUNT_BANNED("ACCOUNT_BANNED", HttpStatus.FORBIDDEN, "Tài khoản đã bị khóa"),
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED", HttpStatus.FORBIDDEN, "Email chưa được xác thực"),
    PLAYLIST_NOT_OWNER("PLAYLIST_NOT_OWNER", HttpStatus.FORBIDDEN, "Bạn không phải chủ sở hữu playlist"),

    // 404 Not Found
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    BLOG_NOT_FOUND("BLOG_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"),
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy bình luận"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"),
    TAG_NOT_FOUND("TAG_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy thẻ"),
    SERIES_NOT_FOUND("SERIES_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy chuỗi bài viết"),
    STORY_NOT_FOUND("STORY_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy story"),
    MOMENT_NOT_FOUND("MOMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy khoảnh khắc"),
    NOTIFICATION_NOT_FOUND("NOTIFICATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy thông báo"),
    CONVERSATION_NOT_FOUND("CONVERSATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện"),
    MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy tin nhắn"),
    SITE_SETTINGS_NOT_FOUND("SITE_SETTINGS_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy cài đặt"),
    STATUS_NOT_FOUND("STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy status"),
    CANVAS_NOT_FOUND("CANVAS_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy canvas"),
    BADGE_NOT_FOUND("BADGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy huy hiệu"),
    PLAYLIST_NOT_FOUND("PLAYLIST_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy playlist"),
    PLAYLIST_SONG_NOT_FOUND("PLAYLIST_SONG_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy bài hát trong playlist"),
    WIDGET_NOT_FOUND("WIDGET_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy widget"),

    // 409 Conflict
    USERNAME_EXISTS("USERNAME_EXISTS", HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại"),
    ALREADY_CHECKED_IN("ALREADY_CHECKED_IN", HttpStatus.CONFLICT, "Bạn đã điểm danh hôm nay"),
    BADGE_ALREADY_AWARDED("BADGE_ALREADY_AWARDED", HttpStatus.CONFLICT, "Huy hiệu đã được trao"),
    EMAIL_EXISTS("EMAIL_EXISTS", HttpStatus.CONFLICT, "Email đã tồn tại"),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "Email đã tồn tại"),
    ALREADY_FOLLOWED("ALREADY_FOLLOWED", HttpStatus.CONFLICT, "Đã theo dõi người dùng này"),
    ALREADY_BOOKMARKED("ALREADY_BOOKMARKED", HttpStatus.CONFLICT, "Đã đánh dấu bài viết này"),
    DATA_CONFLICT("DATA_CONFLICT", HttpStatus.CONFLICT, "Xung đột dữ liệu"),
    PLAYLIST_SONG_ALREADY_EXISTS("PLAYLIST_SONG_ALREADY_EXISTS", HttpStatus.CONFLICT, "Bài hát đã tồn tại trong playlist"),

    // 429 Too Many Requests
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn yêu cầu"),
    STORY_DAILY_LIMIT("STORY_DAILY_LIMIT", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn story trong ngày (5)"),
    PLAYLIST_DAILY_LIMIT("PLAYLIST_DAILY_LIMIT", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn thêm bài trong ngày (5)"),
    CANVAS_RATE_LIMITED("CANVAS_RATE_LIMITED", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn stroke (1/giây)"),

    // Shop errors
    ITEM_NOT_FOUND("ITEM_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy vật phẩm"),
    ITEM_NOT_AVAILABLE("ITEM_NOT_AVAILABLE", HttpStatus.BAD_REQUEST, "Vật phẩm không khả dụng"),
    ITEM_SOLD_OUT("ITEM_SOLD_OUT", HttpStatus.BAD_REQUEST, "Vật phẩm đã bán hết"),
    INVENTORY_NOT_FOUND("INVENTORY_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy vật phẩm trong túi đồ"),
    INSUFFICIENT_COINS("INSUFFICIENT_COINS", HttpStatus.BAD_REQUEST, "Không đủ xu"),
    INSUFFICIENT_GEMS("INSUFFICIENT_GEMS", HttpStatus.BAD_REQUEST, "Không đủ gem"),
    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS", HttpStatus.BAD_REQUEST, "Không đủ tiền"),
    INVALID_CURRENCY("INVALID_CURRENCY", HttpStatus.BAD_REQUEST, "Loại tiền tệ không hợp lệ"),

    // Blind challenge errors
    CHALLENGE_NOT_FOUND("CHALLENGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy thử thách"),
    ALREADY_GUESSED("ALREADY_GUESSED", HttpStatus.CONFLICT, "Bạn đã đoán hôm nay"),
    CHALLENGE_NOT_REVEALED("CHALLENGE_NOT_REVEALED", HttpStatus.BAD_REQUEST, "Thử thách chưa được mở"),

    // Quest errors
    QUEST_NOT_FOUND("QUEST_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy nhiệm vụ"),
    USER_QUEST_NOT_FOUND("USER_QUEST_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy nhiệm vụ của người dùng"),
    QUEST_ALREADY_ASSIGNED("QUEST_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "Nhiệm vụ đã được giao"),
    QUEST_MAX_LIMIT("QUEST_MAX_LIMIT", HttpStatus.BAD_REQUEST, "Đã đạt giới hạn nhiệm vụ tối đa"),
    QUEST_NOT_COMPLETED("QUEST_NOT_COMPLETED", HttpStatus.BAD_REQUEST, "Nhiệm vụ chưa hoàn thành"),
    QUEST_EXPIRED("QUEST_EXPIRED", HttpStatus.BAD_REQUEST, "Nhiệm vụ đã hết hạn"),
    QUEST_ALREADY_CLAIMED("QUEST_ALREADY_CLAIMED", HttpStatus.CONFLICT, "Nhiệm vụ đã được nhận thưởng"),
    QUEST_TYPE_INVALID("QUEST_TYPE_INVALID", HttpStatus.BAD_REQUEST, "Loại nhiệm vụ không hợp lệ"),

    // 500 Internal Server Error
    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Hệ thống gặp sự cố");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
