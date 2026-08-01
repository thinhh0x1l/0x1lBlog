package top.blogapi.engagement.reaction.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReactionRequest(
        @NotNull(message = "Blog ID is required")
        Long blogId,

        @NotBlank(message = "Reaction type is required")
        String type
) {}
