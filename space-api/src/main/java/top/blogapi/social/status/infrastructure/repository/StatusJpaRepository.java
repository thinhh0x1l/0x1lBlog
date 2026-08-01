package top.blogapi.social.status.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.Status;

@Repository
public interface StatusJpaRepository extends JpaRepository<Status, Long> {

    @Modifying
    @Query(value = "UPDATE statuses SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM statuses WHERE user_id = :userId AND created_at >= CURRENT_DATE AND deleted_at IS NULL", nativeQuery = true)
    long countTodayByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM statuses WHERE user_id = :userId AND deleted_at IS NULL", nativeQuery = true)
    long countByUserId(@Param("userId") Long userId);
}
