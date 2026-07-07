package top.blogapi.dto.request.canvas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CanvasRequest {
    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Positive(message = "Width must be positive")
    private Integer width;

    @Positive(message = "Height must be positive")
    private Integer height;

    private Integer x;
    private Integer y;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;

    @Positive(message = "Brush size must be positive")
    private Integer brushSize;
}
