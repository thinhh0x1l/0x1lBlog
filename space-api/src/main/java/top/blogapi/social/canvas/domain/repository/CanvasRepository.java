package top.blogapi.social.canvas.domain.repository;

import top.blogapi.social.canvas.domain.entity.Canvas;

import java.util.List;
import java.util.Optional;

public interface CanvasRepository {

    Optional<Canvas> findById(Long id);

    List<Canvas> findByTypeAndIsActive(String type, boolean isActive);

    Optional<Canvas> findActiveByOwnerId(Long ownerId);

    void insert(Canvas canvas);

    void updateIsActive(Long id, boolean isActive);

    void deleteStrokesByCanvasId(Long canvasId);

    List<Canvas> findExpiredEventCanvases();
}
