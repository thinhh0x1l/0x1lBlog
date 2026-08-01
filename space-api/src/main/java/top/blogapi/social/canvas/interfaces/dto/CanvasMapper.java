package top.blogapi.social.canvas.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.social.canvas.domain.entity.Canvas;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;

@Mapper(componentModel = "spring")
public interface CanvasMapper {
    CanvasResponse toResponse(Canvas canvas);
    CanvasStrokeResponse toStrokeResponse(CanvasStroke stroke);
}
