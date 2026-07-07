package top.blogapi.service.canvas.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Canvas;
import top.blogapi.model.entity.CanvasStroke;
import top.blogapi.repository.CanvasRepository;
import top.blogapi.repository.CanvasStrokeRepository;
import top.blogapi.service.canvas.CanvasService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai CanvasService xử lý tạo canvas, ghi nhận nét vẽ
 * với giới hạn 1 giây và tự động hết hạn canvas sự kiện.
 */
public class CanvasServiceImpl implements CanvasService {

    private final CanvasRepository canvasRepository;
    private final CanvasStrokeRepository canvasStrokeRepository;

    @Override
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

    @Override
    public Canvas findById(Long id) {
        return canvasRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CANVAS_NOT_FOUND));
    }

    @Override
    public List<Canvas> findByTypeAndIsActive(String type, boolean isActive) {
        return canvasRepository.findByTypeAndIsActive(type, isActive);
    }

    @Override
    public Canvas findActiveByOwnerId(Long ownerId) {
        return canvasRepository.findActiveByOwnerId(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.CANVAS_NOT_FOUND));
    }

    @Override
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

    @Override
    public List<CanvasStroke> getStrokes(Long canvasId) {
        if (canvasRepository.findById(canvasId).isEmpty()) {
            throw new AppException(ErrorCode.CANVAS_NOT_FOUND);
        }
        return canvasStrokeRepository.findByCanvasIdOrderByCreatedAt(canvasId);
    }

    @Override
    public void resetCanvas(Long canvasId, Long adminId) {
        Canvas canvas = findById(canvasId);
        canvasRepository.deleteStrokesByCanvasId(canvasId);
    }

    @Override
    public void expireEventCanvases() {
        List<Canvas> expired = canvasRepository.findExpiredEventCanvases();
        for (Canvas canvas : expired) {
            canvasRepository.updateIsActive(canvas.getId(), false);
        }
    }
}
