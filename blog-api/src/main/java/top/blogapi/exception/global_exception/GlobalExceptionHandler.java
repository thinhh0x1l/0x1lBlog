package top.blogapi.exception.global_exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import top.blogapi.dto.response.common.ErrorResponse;
import top.blogapi.exception.business_exception.DomainException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Xử lý DomainException (BlogServiceException, ...)
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex, ServletWebRequest request) {
        log.warn("Domain Exception {}/{} - {}",
                ex.getDomain(), ex.getErrorCode(), ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .domain(ex.getDomain())
                .path(request.getRequest().getRequestURI())
                .context(ex.getContext())
                .timestamp(LocalDateTime.now())
                .entityId(ex.getEntityId())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }
}
