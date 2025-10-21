package top.blogapi.dto.response.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    String errorCode;
    String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" +
            "" +
            "" +
            "")
    LocalDateTime timestamp;
    String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String domain;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String entityId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, Object> context;

}
