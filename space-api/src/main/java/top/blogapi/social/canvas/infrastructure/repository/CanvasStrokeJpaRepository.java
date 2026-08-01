package top.blogapi.social.canvas.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.canvas.domain.entity.CanvasStroke;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanvasStrokeJpaRepository extends JpaRepository<CanvasStroke, Long> {

    List<CanvasStroke> findByCanvasIdOrderByCreatedAtAsc(Long canvasId);

    @Query(value = "SELECT COUNT(*) FROM canvas_strokes WHERE canvas_id = :canvasId", nativeQuery = true)
    long countByCanvasId(@Param("canvasId") Long canvasId);
}
