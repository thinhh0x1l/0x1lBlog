package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.CanvasStroke;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code canvas_strokes}. Xử lý từng nét vẽ trên
 * canvas với truy vấn nét cuối và đếm số lượng.
 */
@Mapper
public interface CanvasStrokeRepository {

    @Select("SELECT * FROM canvas_strokes WHERE id = #{id}")
    Optional<CanvasStroke> findById(Long id);

    @Select("SELECT * FROM canvas_strokes WHERE canvas_id = #{canvasId} ORDER BY created_at ASC")
    List<CanvasStroke> findByCanvasIdOrderByCreatedAt(Long canvasId);

    @Select("SELECT * FROM canvas_strokes WHERE canvas_id = #{canvasId} AND user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    Optional<CanvasStroke> findLastStrokeByUserAndCanvas(@Param("canvasId") Long canvasId, @Param("userId") Long userId);

    @Insert("""
        INSERT INTO canvas_strokes (canvas_id, user_id, x, y, color, brush_size)
        VALUES (#{canvasId}, #{userId}, #{x}, #{y}, #{color}, #{brushSize})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CanvasStroke stroke);

    @Select("SELECT COUNT(*) FROM canvas_strokes WHERE canvas_id = #{canvasId}")
    long countByCanvasId(Long canvasId);
}
