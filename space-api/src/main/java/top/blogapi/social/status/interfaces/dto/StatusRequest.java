package top.blogapi.social.status.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record StatusRequest(
        @NotBlank(message = "Content is required")
        @Size(max = 5000, message = "Content must not exceed 5000 characters")
        String content,

        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl,

        @NotBlank(message = "Visibility is required")
        @Size(max = 20, message = "Visibility must not exceed 20 characters")
        String visibility,

        Long threadId,
        PollRequest poll
) {
    public record PollRequest(
            @NotBlank(message = "Poll question is required")
            @Size(max = 500, message = "Poll question must not exceed 500 characters")
            String question,

            @NotEmpty(message = "Poll options are required")
            List<String> options,

            @NotNull(message = "Poll end time is required")
            Instant endsAt
    ) {}
}
