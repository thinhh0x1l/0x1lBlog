package top.blogapi.social.canvas.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;
import top.blogapi.social.canvas.domain.repository.CanvasStrokeRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CanvasStrokeRepositoryImpl implements CanvasStrokeRepository {

    private final CanvasStrokeJpaRepository jpa;
    private final CanvasStrokeMybatisMapper mybatis;

    @Override
    public Optional<CanvasStroke> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<CanvasStroke> findByCanvasIdOrderByCreatedAt(Long canvasId) {
        return jpa.findByCanvasIdOrderByCreatedAtAsc(canvasId);
    }

    @Override
    public Optional<CanvasStroke> findLastStrokeByUserAndCanvas(Long canvasId, Long userId) {
        return mybatis.findLastStrokeByUserAndCanvas(canvasId, userId);
    }

    @Override
    public void insert(CanvasStroke stroke) {
        if (stroke.getCreatedAt() == null) {
            stroke.setCreatedAt(Instant.now());
        }
        jpa.save(stroke);
    }

    @Override
    public long countByCanvasId(Long canvasId) {
        return jpa.countByCanvasId(canvasId);
    }
}
