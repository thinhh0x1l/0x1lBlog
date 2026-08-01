package top.blogapi.user.profile.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WidgetUpdateRequest(
        @NotBlank(message = "Widget type is required")
        @Size(max = 100, message = "Widget type must not exceed 100 characters")
        String widgetType,

        Boolean isVisible,
        Integer sortOrder,

        @Size(max = 2000, message = "Config must not exceed 2000 characters")
        String config
) {}
