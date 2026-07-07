package top.blogapi.dto.skill;

import lombok.Data;

import java.util.List;

/**
 * DTO đại diện cho tiến độ của người dùng trong một nhóm kỹ năng.
 */
@Data
public class SkillProgressDTO {
    private Long categoryId;
    private String categoryName;
    private Integer totalPoints;
    private List<Long> unlockedSkillIds;
}
