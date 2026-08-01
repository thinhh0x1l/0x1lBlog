package top.blogapi.social.canvas.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.blogapi.social.canvas.domain.entity.Canvas;

import java.util.List;

@Mapper
public interface CanvasMybatisMapper {

    @Update("DELETE FROM canvas_strokes WHERE canvas_id = #{canvasId}")
    void deleteStrokesByCanvasId(@Param("canvasId") Long canvasId);

    @Select("SELECT * FROM canvases WHERE type = 'event' AND is_active = TRUE AND ends_at IS NOT NULL AND ends_at < NOW()")
    List<Canvas> findExpiredEventCanvases();
}
