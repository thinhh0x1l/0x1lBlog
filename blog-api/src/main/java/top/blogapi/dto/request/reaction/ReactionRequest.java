package top.blogapi.dto.request.reaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReactionRequest {

    @NotNull(message = "Blog ID is required")
    private Long blogId;

    @NotBlank(message = "Reaction type is required")
    private String type;
}
