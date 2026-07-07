package top.blogapi.dto.profile;

import lombok.Data;

/**
 * DTO đại diện cho một widget trên trang cá nhân với trạng thái hiển thị và cấu hình.
 */
@Data
public class WidgetDTO {
    private Long id;
    private String widgetType;
    private Boolean isVisible;
    private Integer sortOrder;
    private String config;
}
