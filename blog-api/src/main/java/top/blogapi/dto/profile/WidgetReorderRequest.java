package top.blogapi.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WidgetReorderRequest {
    @NotBlank(message = "Widget type is required")
    @Size(max = 100, message = "Widget type must not exceed 100 characters")
    private String widgetType;

    @NotNull(message = "Sort order is required")
    private Integer sortOrder;
}
