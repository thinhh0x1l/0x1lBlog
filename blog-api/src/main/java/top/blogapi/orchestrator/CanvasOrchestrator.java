package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.canvas.CanvasRequest;
import top.blogapi.model.entity.Canvas;
import top.blogapi.model.entity.CanvasStroke;
import top.blogapi.service.canvas.CanvasService;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Orchestrates canvas creation, stroke management, and canvas lifecycle for drawing features.
 */
@Component
@RequiredArgsConstructor
public class CanvasOrchestrator {

    private final CanvasService canvasService;

    @Transactional
    public Canvas createCanvas(CanvasRequest request, Long userId) {
        Canvas canvas = new Canvas();
        canvas.setType(request.getType());
        canvas.setTitle(request.getTitle());
        canvas.setWidth(request.getWidth() != null ? request.getWidth() : 200);
        canvas.setHeight(request.getHeight() != null ? request.getHeight() : 200);
        if (!"community".equals(request.getType())) {
            canvas.setOwnerId(userId);
        }
        if ("event".equals(request.getType())) {
            canvas.setStartsAt(OffsetDateTime.now());
            canvas.setEndsAt(OffsetDateTime.now().plusDays(7));
        }
        return canvasService.create(canvas);
    }

    public Canvas getCanvas(Long id) {
        return canvasService.findById(id);
    }

    public Canvas getCanvasByType(String type) {
        List<Canvas> canvases = canvasService.findByTypeAndIsActive(type, true);
        if (canvases.isEmpty()) {
            Canvas canvas = new Canvas();
            canvas.setType(type);
            canvas.setTitle(type.equals("profile") ? "Profile Canvas" : "Community Canvas");
            canvas.setWidth(type.equals("profile") ? 200 : 500);
            canvas.setHeight(type.equals("profile") ? 200 : 500);
            return canvasService.create(canvas);
        }
        return canvases.getFirst();
    }

    @Transactional
    public CanvasStroke addStroke(Long canvasId, CanvasRequest request, Long userId) {
        CanvasStroke stroke = new CanvasStroke();
        stroke.setCanvasId(canvasId);
        stroke.setUserId(userId);
        stroke.setX(request.getX());
        stroke.setY(request.getY());
        stroke.setColor(request.getColor() != null ? request.getColor() : "#000000");
        stroke.setBrushSize(request.getBrushSize() != null ? request.getBrushSize() : 3);
        return canvasService.addStroke(stroke);
    }

    public List<CanvasStroke> getStrokes(Long canvasId) {
        return canvasService.getStrokes(canvasId);
    }

    @Transactional
    public void resetCanvas(Long canvasId) {
        canvasService.resetCanvas(canvasId, null);
    }
}
