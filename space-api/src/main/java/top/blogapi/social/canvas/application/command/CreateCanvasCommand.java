package top.blogapi.social.canvas.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.social.canvas.interfaces.dto.CanvasRequest;
import top.blogapi.social.canvas.domain.entity.Canvas;
import top.blogapi.social.canvas.domain.service.CanvasService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateCanvasCommand {

    private final CanvasService canvasService;

    @Transactional
    public Canvas execute(CanvasRequest request, Long userId) {
        Canvas canvas = new Canvas();
        canvas.setType(request.type());
        canvas.setTitle(request.title());
        canvas.setWidth(request.width() != null ? request.width() : 200);
        canvas.setHeight(request.height() != null ? request.height() : 200);
        if (!"community".equals(request.type())) {
            canvas.setOwnerId(userId);
        }
        if ("event".equals(request.type())) {
            canvas.setStartsAt(Instant.now());
            canvas.setEndsAt(Instant.now().plusSeconds(7 * 24 * 3600));
        }
        return canvasService.create(canvas);
    }
}
