package top.blogapi.social.playlist.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReorderRequest(
        @NotEmpty(message = "Song IDs are required")
        List<Long> songIds
) {}
