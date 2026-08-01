package top.blogapi.social.canvas.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.canvas.domain.entity.Canvas;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;
import top.blogapi.social.canvas.domain.repository.CanvasRepository;
import top.blogapi.social.canvas.domain.repository.CanvasStrokeRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final CanvasStrokeRepository canvasStrokeRepository;

    public Canvas create(Canvas canvas) {
        if (canvas.getType() == null) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Canvas type is required");
        }
        if ("profile".equals(canvas.getType()) && canvas.getOwnerId() != null) {
            canvasRepository.findActiveByOwnerId(canvas.getOwnerId())
                    .ifPresent(existing -> {
                        throw new AppException(ErrorCode.DATA_CONFLICT, "User already has a profile canvas");
                    });
        }
        canvas.setIsActive(true);
        canvasRepository.insert(canvas);
        return canvas;
    }

    public Canvas findById(Long id) {
        return canvasRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CANVAS_NOT_FOUND));
    }

    public List<Canvas> findByTypeAndIsActive(String type, boolean isActive) {
        return canvasRepository.findByTypeAndIsActive(type, isActive);
    }

    public Canvas findActiveByOwnerId(Long ownerId) {
        return canvasRepository.findActiveByOwnerId(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.CANVAS_NOT_FOUND));
    }

    public CanvasStroke addStroke(CanvasStroke stroke) {
        Canvas canvas = findById(stroke.getCanvasId());
        if (!Boolean.TRUE.equals(canvas.getIsActive())) {
            throw new AppException(ErrorCode.CANVAS_NOT_FOUND, "Canvas is not active");
        }

        canvasStrokeRepository.findLastStrokeByUserAndCanvas(stroke.getCanvasId(), stroke.getUserId())
                .ifPresent(lastStroke -> {
                    if (lastStroke.getCreatedAt() != null
                            && Duration.between(lastStroke.getCreatedAt(), OffsetDateTime.now()).toSeconds() < 1) {
                        throw new AppException(ErrorCode.CANVAS_RATE_LIMITED);
                    }
                });

        canvasStrokeRepository.insert(stroke);
        return stroke;
    }

    public List<CanvasStroke> getStrokes(Long canvasId) {
        if (canvasRepository.findById(canvasId).isEmpty()) {
            throw new AppException(ErrorCode.CANVAS_NOT_FOUND);
        }
        return canvasStrokeRepository.findByCanvasIdOrderByCreatedAt(canvasId);
    }

    public void resetCanvas(Long canvasId, Long adminId) {
        Canvas canvas = findById(canvasId);
        canvasRepository.deleteStrokesByCanvasId(canvasId);
    }

    public void expireEventCanvases() {
        List<Canvas> expired = canvasRepository.findExpiredEventCanvases();
        for (Canvas canvas : expired) {
            canvasRepository.updateIsActive(canvas.getId(), false);
        }
    }
}
