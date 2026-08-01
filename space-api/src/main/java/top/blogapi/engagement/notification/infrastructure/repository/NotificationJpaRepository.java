package top.blogapi.engagement.notification.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.notification.domain.entity.Notification;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = FALSE", nativeQuery = true)
    long countUnreadByUserId(@Param("userId") Long userId);

    long countByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE notifications SET is_read = TRUE WHERE user_id = :userId AND is_read = FALSE", nativeQuery = true)
    void markAllReadByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE notifications SET is_read = TRUE WHERE id = :id", nativeQuery = true)
    void markReadById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM notifications WHERE created_at < NOW() - INTERVAL '90 days'", nativeQuery = true)
    void deleteOlderThan90Days();
}
