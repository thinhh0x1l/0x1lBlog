package top.blogapi.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WidgetUpdateRequest {
    @NotBlank(message = "Widget type is required")
    @Size(max = 100, message = "Widget type must not exceed 100 characters")
    private String widgetType;

    private Boolean isVisible;
    private Integer sortOrder;

    @Size(max = 2000, message = "Config must not exceed 2000 characters")
    private String config;
}
