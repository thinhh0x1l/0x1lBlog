package top.blogapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     *  400 - BAD REQUEST
     * */
    ABOUT_UPDATE_PARTIAL("ABOUT_UPDATE_PARTIAL", HttpStatus.BAD_REQUEST,"Update thiếu row"),

    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "Dữ liệu không đúng"),

    CONTENT_REQUIRED("CONTENT_REQUIRED", HttpStatus.BAD_REQUEST, "Nội dung cần phải được điền"),

    /**
     *  404 - NOT FOUND
     * */

    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "Không tìm thấy User"),

    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Comment not found"),

    BLOG_NOT_FOUND("BLOG_NOT_FOUND", HttpStatus.NOT_FOUND, "Blog not found"),

    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND, "Category not found"),

    DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "Email already exists"),

    TAG_NOT_FOUND("TAG_NOT_FOUND", HttpStatus.NOT_FOUND, "Tag not found"),

    SITE_SETTINGS_NOT_FOUND("SITE_SETTINGS_NOT_FOUND", HttpStatus.NOT_FOUND, "SiteSetting not found"),

    MOMENT_NOT_FOUND("MOMENT_NOT_FOUND", HttpStatus.NOT_FOUND,"Moment not found"),

    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Bạn cần phải đăng nhập"),
    DATA_CONFLICT("DATA_CONFLICT", HttpStatus.CONFLICT, "Xung đột dữ liệu"),
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "Bạn không được phép truy cập"),
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