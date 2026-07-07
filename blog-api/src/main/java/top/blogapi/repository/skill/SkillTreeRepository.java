package top.blogapi.repository.skill;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.blogapi.model.entity.skill.SkillTree;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code skill_trees}. Cung cấp định nghĩa cây kỹ
 * năng có thứ tự được nhóm theo danh mục.
 */
@Mapper
public interface SkillTreeRepository {

    @Select("SELECT * FROM skill_trees WHERE category_id = #{categoryId} ORDER BY sort_order")
    List<SkillTree> findByCategoryIdOrderBySortOrder(Long categoryId);

    @Select("SELECT * FROM skill_trees ORDER BY category_id, sort_order")
    List<SkillTree> findAllByOrderByCategoryIdSortOrder();

    @Select("SELECT * FROM skill_trees WHERE id = #{id}")
    java.util.Optional<SkillTree> findById(Long id);
}
