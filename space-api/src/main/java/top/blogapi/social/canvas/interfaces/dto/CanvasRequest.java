package top.blogapi.social.canvas.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CanvasRequest(
        @NotBlank(message = "Type is required")
        @Size(max = 50, message = "Type must not exceed 50 characters")
        String type,

        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Positive(message = "Width must be positive")
        Integer width,

        @Positive(message = "Height must be positive")
        Integer height,

        Integer x,
        Integer y,

        @Size(max = 50, message = "Color must not exceed 50 characters")
        String color,

        @Positive(message = "Brush size must be positive")
        Integer brushSize
) {}
