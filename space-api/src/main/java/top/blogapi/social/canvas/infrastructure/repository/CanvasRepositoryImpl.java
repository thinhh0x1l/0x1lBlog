package top.blogapi.social.canvas.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.canvas.domain.entity.Canvas;
import top.blogapi.social.canvas.domain.repository.CanvasRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CanvasRepositoryImpl implements CanvasRepository {

    private final CanvasJpaRepository jpa;
    private final CanvasMybatisMapper mybatis;

    @Override
    public Optional<Canvas> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Canvas> findByTypeAndIsActive(String type, boolean isActive) {
        return jpa.findByTypeAndIsActive(type, isActive);
    }

    @Override
    public Optional<Canvas> findActiveByOwnerId(Long ownerId) {
        return jpa.findFirstByOwnerIdAndIsActiveTrue(ownerId);
    }

    @Override
    public void insert(Canvas canvas) {
        if (canvas.getCreatedAt() == null) {
            canvas.setCreatedAt(Instant.now());
        }
        jpa.save(canvas);
    }

    @Override
    public void updateIsActive(Long id, boolean isActive) {
        jpa.updateIsActive(id, isActive);
    }

    @Override
    public void deleteStrokesByCanvasId(Long canvasId) {
        mybatis.deleteStrokesByCanvasId(canvasId);
    }

    @Override
    public List<Canvas> findExpiredEventCanvases() {
        return mybatis.findExpiredEventCanvases();
    }
}
