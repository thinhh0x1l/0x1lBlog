package top.blogapi.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Phản hồi lỗi chuẩn gửi về client khi có ngoại lệ.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private Map<String, String> errors;
    private String path;
    private String traceId;
    private LocalDateTime timestamp;

    public ErrorResponse() {}

    public ErrorResponse(int status, String code, String message, Map<String, String> errors,
                         String path, String traceId, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.path = path;
        this.traceId = traceId;
        this.timestamp = timestamp;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private int status;
        private String code;
        private String message;
        private Map<String, String> errors;
        private String path;
        private String traceId;
        private LocalDateTime timestamp;

        ErrorResponseBuilder() {}

        public ErrorResponseBuilder status(int status) { this.status = status; return this; }
        public ErrorResponseBuilder code(String code) { this.code = code; return this; }
        public ErrorResponseBuilder error(String error) { this.code = error; return this; }
        public ErrorResponseBuilder message(String message) { this.message = message; return this; }
        public ErrorResponseBuilder errors(Map<String, String> errors) { this.errors = errors; return this; }
        public ErrorResponseBuilder path(String path) { this.path = path; return this; }
        public ErrorResponseBuilder traceId(String traceId) { this.traceId = traceId; return this; }
        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public ErrorResponse build() {
            return new ErrorResponse(status, code, message, errors, path, traceId, timestamp);
        }
    }

    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getError() { return code; }
    public String getMessage() { return message; }
    public Map<String, String> getErrors() { return errors; }
    public String getPath() { return path; }
    public String getTraceId() { return traceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
