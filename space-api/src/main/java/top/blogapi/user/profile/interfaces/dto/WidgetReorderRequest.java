package top.blogapi.user.profile.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WidgetReorderRequest(
        @NotBlank(message = "Widget type is required")
        @Size(max = 100, message = "Widget type must not exceed 100 characters")
        String widgetType,

        @NotNull(message = "Sort order is required")
        Integer sortOrder
) {}
