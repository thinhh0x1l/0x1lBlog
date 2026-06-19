package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Integer blogCount;
    private Boolean isVisible;
    private OffsetDateTime createdAt;
}
