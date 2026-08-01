package top.blogapi.social.canvas.interfaces.dto;

import java.time.Instant;

public record CanvasStrokeResponse(
        Long id,
        Long canvasId,
        Long userId,
        String color,
        Integer strokeWidth,
        String points,
        Instant createdAt
) {}
