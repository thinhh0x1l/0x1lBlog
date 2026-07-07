package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho một nét vẽ riêng lẻ trên canvas.
 */
@Data
public class CanvasStrokeResponse {
    private Long id;
    private Long canvasId;
    private Long userId;
    private int x;
    private int y;
    private String color;
    private int brushSize;
    private OffsetDateTime createdAt;
}
