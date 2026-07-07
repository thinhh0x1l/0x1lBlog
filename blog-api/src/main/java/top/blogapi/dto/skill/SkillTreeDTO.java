package top.blogapi.dto.skill;

import lombok.Data;

/**
 * DTO đại diện cho một nút kỹ năng trong cây kỹ năng với trạng thái mở khóa.
 */
@Data
public class SkillTreeDTO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String perkType;
    private Integer pointsRequired;
    private Integer sortOrder;
    private Boolean unlocked;
}
