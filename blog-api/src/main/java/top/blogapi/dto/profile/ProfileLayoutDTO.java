package top.blogapi.dto.profile;

import lombok.Data;

import java.util.List;

/**
 * DTO đại diện cho cấu hình bố cục trang cá nhân, bao gồm widget và chế độ game.
 */
@Data
public class ProfileLayoutDTO {
    private List<WidgetDTO> widgets;
    private Boolean gameMode;
    private String profileLayout;
}
