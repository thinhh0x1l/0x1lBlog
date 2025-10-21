package top.blogapi.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BaseException extends RuntimeException {
    String errorCode;
    Map<String, Object> context;
    HttpStatus httpStatus;
    String messageKey;
    Object[] messageArgs;

    protected BaseException(Builder<?> builder ){
        super(builder.message, builder.cause);
        this.errorCode = builder.errorCode;
        this.context = builder.context != null ?
                Map.copyOf(builder.context): Map.of();
        this.httpStatus = builder.httpStatus;
        this.messageKey = builder.messageKey;
        this.messageArgs = builder.messageArgs;
    }

    @FieldDefaults(level = AccessLevel.PROTECTED)
    public static abstract class Builder<T extends Builder<T>>{
        String errorCode;
        String message;
        Throwable cause;
        Map<String, Object> context;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageKey;
        Object[] messageArgs;

        public T errorCode(String errorCode){
            this.errorCode = errorCode;
            return this.self();
        }

        public T message(String message){
            this.message = message;
            return this.self();
        }

        public T cause(Throwable cause){
            this.cause = cause;
            return this.self();
        }

        public T context (String key, Object value){
            if(this.context == null)
                this.context = new HashMap<>();
            this.context.put(key, value);
            return this.self();
        }

        public T httpStatus(HttpStatus httpStatus){
            this.httpStatus = httpStatus;
            return this.self();
        }
        public T messageKey(String messageKey, Object[] messageArgs){
            this.messageKey = messageKey;
            this.messageArgs = messageArgs;
            return this.self();
        }

        protected abstract T self();
        public abstract BaseException build();
    }
}
