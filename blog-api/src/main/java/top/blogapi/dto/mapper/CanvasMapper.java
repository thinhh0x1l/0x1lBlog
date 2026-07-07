package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.CanvasResponse;
import top.blogapi.dto.response.CanvasStrokeResponse;
import top.blogapi.model.entity.Canvas;
import top.blogapi.model.entity.CanvasStroke;

/**
 * Mapper MapStruct để chuyển đổi entity Canvas và CanvasStroke sang DTO.
 */
@Mapper(componentModel = "spring")
public interface CanvasMapper {

    CanvasResponse toResponse(Canvas canvas);

    CanvasStrokeResponse toStrokeResponse(CanvasStroke stroke);
}
