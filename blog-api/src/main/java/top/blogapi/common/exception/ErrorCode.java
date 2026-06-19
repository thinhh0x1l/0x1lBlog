package top.blogapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

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

    // 404 Not Found
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    BLOG_NOT_FOUND("BLOG_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"),
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy bình luận"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"),
    TAG_NOT_FOUND("TAG_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy thẻ"),
    SERIES_NOT_FOUND("SERIES_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy chuỗi bài viết"),
    MOMENT_NOT_FOUND("MOMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy khoảnh khắc"),
    NOTIFICATION_NOT_FOUND("NOTIFICATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy thông báo"),
    CONVERSATION_NOT_FOUND("CONVERSATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện"),
    MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy tin nhắn"),
    SITE_SETTINGS_NOT_FOUND("SITE_SETTINGS_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy cài đặt"),

    // 409 Conflict
    USERNAME_EXISTS("USERNAME_EXISTS", HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại"),
    EMAIL_EXISTS("EMAIL_EXISTS", HttpStatus.CONFLICT, "Email đã tồn tại"),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "Email đã tồn tại"),
    ALREADY_FOLLOWED("ALREADY_FOLLOWED", HttpStatus.CONFLICT, "Đã theo dõi người dùng này"),
    ALREADY_BOOKMARKED("ALREADY_BOOKMARKED", HttpStatus.CONFLICT, "Đã đánh dấu bài viết này"),
    DATA_CONFLICT("DATA_CONFLICT", HttpStatus.CONFLICT, "Xung đột dữ liệu"),

    // 429 Too Many Requests
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn yêu cầu"),

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
