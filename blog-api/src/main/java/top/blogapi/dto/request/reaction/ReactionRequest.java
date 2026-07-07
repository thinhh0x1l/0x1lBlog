package top.blogapi.dto.request.reaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request body để bật/tắt cảm xúc trên một bài viết blog.
 */
@Data
public class ReactionRequest {

    @NotNull(message = "Blog ID is required")
    private Long blogId;

    @NotBlank(message = "Reaction type is required")
    private String type;
}
