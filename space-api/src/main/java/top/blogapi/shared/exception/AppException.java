package top.blogapi.shared.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> context;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.context = Map.of();
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.context = Map.of();
    }

    public AppException(ErrorCode errorCode, String customMessage, Map<String, Object> context) {
        super(customMessage);
        this.errorCode = errorCode;
        this.context = context == null ? Map.of() : Map.copyOf(context);
    }

    public AppException addContext(String key, Object value) {
        Map<String, Object> newContext = new HashMap<>(this.context);
        newContext.put(key, value);
        return new AppException(this.errorCode, getMessage(), newContext);
    }
}
