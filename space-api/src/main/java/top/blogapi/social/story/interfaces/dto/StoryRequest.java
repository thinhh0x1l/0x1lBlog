package top.blogapi.social.story.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StoryRequest(
        @NotBlank(message = "Media URL is required")
        @Size(max = 500, message = "Media URL must not exceed 500 characters")
        String mediaUrl,

        @NotBlank(message = "Media type is required")
        @Size(max = 20, message = "Media type must not exceed 20 characters")
        String mediaType,

        @Size(max = 1000, message = "Text content must not exceed 1000 characters")
        String textContent,

        @NotBlank(message = "Visibility is required")
        @Size(max = 20, message = "Visibility must not exceed 20 characters")
        String visibility
) {}
