package top.blogapi.social.canvas.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;

import java.util.Optional;

@Mapper
public interface CanvasStrokeMybatisMapper {

    @Select("SELECT * FROM canvas_strokes WHERE canvas_id = #{canvasId} AND user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    Optional<CanvasStroke> findLastStrokeByUserAndCanvas(@Param("canvasId") Long canvasId, @Param("userId") Long userId);
}
