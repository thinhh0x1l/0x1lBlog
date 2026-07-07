package top.blogapi.common.exception;

/**
 * Ném ra khi client gửi yêu cầu không hợp lệ (HTTP 400).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
