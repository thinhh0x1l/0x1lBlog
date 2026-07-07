package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Canvas;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code canvases}. Quản lý canvas cộng tác với
 * nhiều loại khác nhau (profile, community, event).
 */
@Mapper
public interface CanvasRepository {

    @Select("SELECT * FROM canvases WHERE id = #{id}")
    Optional<Canvas> findById(Long id);

    @Select("SELECT * FROM canvases WHERE type = #{type} AND is_active = #{isActive}")
    List<Canvas> findByTypeAndIsActive(@Param("type") String type, @Param("isActive") boolean isActive);

    @Select("SELECT * FROM canvases WHERE owner_id = #{ownerId} AND is_active = TRUE")
    Optional<Canvas> findActiveByOwnerId(Long ownerId);

    @Insert("""
        INSERT INTO canvases (type, title, width, height, owner_id, starts_at, ends_at, is_active)
        VALUES (#{type}, #{title}, #{width}, #{height}, #{ownerId}, #{startsAt}, #{endsAt}, #{isActive})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Canvas canvas);

    @Update("UPDATE canvases SET is_active = #{isActive} WHERE id = #{id}")
    int updateIsActive(@Param("id") Long id, @Param("isActive") boolean isActive);

    @Update("DELETE FROM canvas_strokes WHERE canvas_id = #{canvasId}")
    int deleteStrokesByCanvasId(Long canvasId);

    @Select("SELECT * FROM canvases WHERE type = 'event' AND is_active = TRUE AND ends_at IS NOT NULL AND ends_at < NOW()")
    List<Canvas> findExpiredEventCanvases();
}
