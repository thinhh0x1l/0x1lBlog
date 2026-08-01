package top.blogapi.social.canvas.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.canvas.domain.entity.Canvas;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanvasJpaRepository extends JpaRepository<Canvas, Long> {

    List<Canvas> findByTypeAndIsActive(String type, boolean isActive);

    Optional<Canvas> findFirstByOwnerIdAndIsActiveTrue(Long ownerId);

    @Modifying
    @Query(value = "UPDATE canvases SET is_active = :isActive WHERE id = :id", nativeQuery = true)
    void updateIsActive(@Param("id") Long id, @Param("isActive") boolean isActive);
}
