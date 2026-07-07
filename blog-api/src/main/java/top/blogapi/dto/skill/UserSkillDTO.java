package top.blogapi.dto.skill;

import lombok.Data;

import java.util.List;

/**
 * DTO tổng hợp tiến độ kỹ năng của người dùng trên tất cả nhóm và cây kỹ năng.
 */
@Data
public class UserSkillDTO {
    private List<SkillProgressDTO> progress;
    private List<SkillTreeDTO> skillTrees;
}
