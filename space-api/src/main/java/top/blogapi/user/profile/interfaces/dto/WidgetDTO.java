package top.blogapi.user.profile.interfaces.dto;

public record WidgetDTO(
        Long id,
        String widgetType,
        Boolean isVisible,
        Integer sortOrder,
        String config
) {}
