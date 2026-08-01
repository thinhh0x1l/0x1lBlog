package top.blogapi.social.playlist.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddSongRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Artist is required")
        @Size(max = 200, message = "Artist must not exceed 200 characters")
        String artist,

        @NotBlank(message = "Source is required")
        @Size(max = 50, message = "Source must not exceed 50 characters")
        String source,

        @NotBlank(message = "Source ID is required")
        @Size(max = 200, message = "Source ID must not exceed 200 characters")
        String sourceId,

        @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
        String thumbnailUrl,

        @Positive(message = "Duration must be positive")
        Integer durationSec
) {}
