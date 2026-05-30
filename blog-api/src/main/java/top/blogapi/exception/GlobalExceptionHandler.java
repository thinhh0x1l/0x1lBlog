package top.blogapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import top.blogapi.util.StringUtils;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(
            AppException ex,
            HttpServletRequest request
    ) {
        ErrorCode error = ex.getErrorCode();

        ApiErrorResponse response = new ApiErrorResponse(
                error.getCode(),
                ex.getMessage(),
                error.getHttpStatus().value(),
                request.getRequestURI(),
                StringUtils.isEmpty(request.getHeader("X-Trace-Id"))
                        ? UUID.randomUUID().toString()
                        : request.getHeader("X-Trace-Id"),
                LocalDateTime.now(),
                ex.getContext()
        );

        log.warn(
                "Business exception. path={}, code={}, details={}",
                request.getRequestURI(),
                error.getCode(),
                ex.getContext()
        );

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        Map<String, Object> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (oldVal, newVal) -> oldVal
                ));

        ApiErrorResponse response = new ApiErrorResponse(
                ErrorCode.INVALID_INPUT.getCode(),
                "Validation failed",
                ErrorCode.INVALID_INPUT.getHttpStatus().value(),
                request.getRequestURI(),
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                details
        );
        log.error("Request URL : {}", request.getRequestURL(), ex);
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus()).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> usernameNotFoundExceptionHandler(HttpServletRequest request,
                                                                             UsernameNotFoundException e) {
        log.error("Request URL : {}", request.getRequestURL(), e);
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.toString(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(),
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknownException(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                ErrorCode.INTERNAL_ERROR.getCode(),
                ErrorCode.INTERNAL_ERROR.getMessage(),
                ErrorCode.INTERNAL_ERROR.getHttpStatus().value(),
                request.getRequestURI(),
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                Map.of(
                        "error", ex.getClass().getSimpleName(),
                        "messageCause", ex.getCause() != null ? ex.getCause().getMessage() : "null")
        );
        log.error("Request URL : {}", request.getRequestURL(), ex);
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getHttpStatus()).body(response);
    }


}