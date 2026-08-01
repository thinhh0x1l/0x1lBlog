package top.blogapi.social.canvas.domain.repository;

import top.blogapi.social.canvas.domain.entity.CanvasStroke;

import java.util.List;
import java.util.Optional;

public interface CanvasStrokeRepository {

    Optional<CanvasStroke> findById(Long id);

    List<CanvasStroke> findByCanvasIdOrderByCreatedAt(Long canvasId);

    Optional<CanvasStroke> findLastStrokeByUserAndCanvas(Long canvasId, Long userId);

    void insert(CanvasStroke stroke);

    long countByCanvasId(Long canvasId);
}
