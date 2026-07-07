package top.blogapi.service.canvas;

import top.blogapi.model.entity.Canvas;
import top.blogapi.model.entity.CanvasStroke;

import java.util.List;

/**
 * Giao diện service cho thao tác vẽ canvas, hỗ trợ canvas hồ sơ,
 * cộng đồng và sự kiện với ghi nhận nét vẽ và giới hạn tốc độ.
 */
public interface CanvasService {

    Canvas create(Canvas canvas);

    Canvas findById(Long id);

    List<Canvas> findByTypeAndIsActive(String type, boolean isActive);

    Canvas findActiveByOwnerId(Long ownerId);

    CanvasStroke addStroke(CanvasStroke stroke);

    List<CanvasStroke> getStrokes(Long canvasId);

    void resetCanvas(Long canvasId, Long adminId);

    void expireEventCanvases();
}
