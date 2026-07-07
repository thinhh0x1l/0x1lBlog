package top.blogapi.common.exception;

/**
 * Ném ra khi không tìm thấy tài nguyên yêu cầu (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("Không tìm thấy %s với %s: '%s'", resource, field, value));
    }
}
