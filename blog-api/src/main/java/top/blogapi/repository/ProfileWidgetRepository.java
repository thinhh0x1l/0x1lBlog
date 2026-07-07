package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.profile.ProfileWidget;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code profile_widgets}. Quản lý hiển thị, sắp
 * xếp và cấu hình widget hồ sơ cho từng người dùng.
 */
@Mapper
public interface ProfileWidgetRepository {

    @Select("SELECT * FROM profile_widgets WHERE user_id = #{userId} ORDER BY sort_order")
    List<ProfileWidget> findByUserIdOrderBySortOrder(@Param("userId") Long userId);

    @Select("SELECT * FROM profile_widgets WHERE user_id = #{userId} AND widget_type = #{widgetType}")
    Optional<ProfileWidget> findByUserIdAndWidgetType(@Param("userId") Long userId, @Param("widgetType") String widgetType);

    @Insert("""
        INSERT INTO profile_widgets (user_id, widget_type, is_visible, sort_order, config, created_at, updated_at)
        VALUES (#{userId}, #{widgetType}, #{isVisible}, #{sortOrder}, #{config}::jsonb, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProfileWidget widget);

    @Update("""
        UPDATE profile_widgets SET is_visible = #{isVisible}, sort_order = #{sortOrder},
                                   config = #{config}::jsonb, updated_at = NOW()
        WHERE id = #{id}
    """)
    int update(ProfileWidget widget);

    @Delete("DELETE FROM profile_widgets WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM profile_widgets WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT EXISTS(SELECT 1 FROM profile_widgets WHERE user_id = #{userId} AND widget_type = #{widgetType})")
    boolean existsByUserIdAndWidgetType(@Param("userId") Long userId, @Param("widgetType") String widgetType);

    @Update("UPDATE profile_widgets SET sort_order = #{sortOrder}, updated_at = NOW() WHERE id = #{id}")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);
}
