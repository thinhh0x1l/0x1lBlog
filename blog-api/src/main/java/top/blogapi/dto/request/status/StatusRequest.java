package top.blogapi.dto.request.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class StatusRequest {
    @NotBlank(message = "Content is required")
    @Size(max = 5000, message = "Content must not exceed 5000 characters")
    private String content;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @NotBlank(message = "Visibility is required")
    @Size(max = 20, message = "Visibility must not exceed 20 characters")
    private String visibility;

    private Long threadId;
    private PollRequest poll;

    @Data
    public static class PollRequest {
        @NotBlank(message = "Poll question is required")
        @Size(max = 500, message = "Poll question must not exceed 500 characters")
        private String question;

        @NotEmpty(message = "Poll options are required")
        private List<String> options;

        @NotNull(message = "Poll end time is required")
        private OffsetDateTime endsAt;
    }
}
