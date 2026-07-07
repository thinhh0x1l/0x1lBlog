package top.blogapi.common.exception;

/**
 * Ném ra khi client vượt quá giới hạn yêu cầu (HTTP 429).
 */
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
