package top.blogapi.common.exception;

/**
 * Ném ra khi xác thực bắt buộc nhưng thiếu hoặc không hợp lệ (HTTP 401).
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
