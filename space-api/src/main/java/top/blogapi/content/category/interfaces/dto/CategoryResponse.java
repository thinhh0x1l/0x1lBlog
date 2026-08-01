package top.blogapi.content.category.interfaces.dto;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        String icon,
        String color,
        Integer sortOrder,
        Integer blogCount,
        Boolean isVisible,
        Instant createdAt
) {}
