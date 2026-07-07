package top.blogapi.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Target type is required")
    @Size(max = 50, message = "Target type must not exceed 50 characters")
    private String targetType;

    @NotNull(message = "Target ID is required")
    private Long targetId;

    private Long parentId;

    @NotBlank(message = "Content is required")
    @Size(max = 5000, message = "Content must not exceed 5000 characters")
    private String content;
}
