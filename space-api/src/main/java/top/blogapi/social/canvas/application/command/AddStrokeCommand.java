package top.blogapi.social.canvas.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.social.canvas.interfaces.dto.CanvasRequest;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;
import top.blogapi.social.canvas.domain.service.CanvasService;

@Service
@RequiredArgsConstructor
public class AddStrokeCommand {

    private final CanvasService canvasService;

    @Transactional
    public CanvasStroke execute(Long canvasId, CanvasRequest request, Long userId) {
        CanvasStroke stroke = new CanvasStroke();
        stroke.setCanvasId(canvasId);
        stroke.setUserId(userId);
        stroke.setX(request.x());
        stroke.setY(request.y());
        stroke.setColor(request.color() != null ? request.color() : "#000000");
        stroke.setBrushSize(request.brushSize() != null ? request.brushSize() : 3);
        return canvasService.addStroke(stroke);
    }
}
