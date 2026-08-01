package top.blogapi.social.canvas.interfaces.dto;

import java.time.Instant;

public record CanvasResponse(
        Long id,
        Long ownerId,
        String title,
        Integer width,
        Integer height,
        String backgroundColor,
        Instant createdAt
) {}
