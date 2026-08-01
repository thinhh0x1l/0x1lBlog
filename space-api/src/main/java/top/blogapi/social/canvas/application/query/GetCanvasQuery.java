package top.blogapi.social.canvas.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.social.canvas.domain.entity.Canvas;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;
import top.blogapi.social.canvas.domain.service.CanvasService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCanvasQuery {

    private final CanvasService canvasService;

    public Canvas execute(Long id) {
        return canvasService.findById(id);
    }

    public Canvas getByType(String type) {
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

    public List<CanvasStroke> getStrokes(Long canvasId) {
        return canvasService.getStrokes(canvasId);
    }

    @Transactional
    public void resetCanvas(Long canvasId) {
        canvasService.resetCanvas(canvasId, null);
    }
}
