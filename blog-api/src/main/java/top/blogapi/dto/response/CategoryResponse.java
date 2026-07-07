package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho danh mục blog với siêu dữ liệu hiển thị và số lượng bài viết.
 */
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
