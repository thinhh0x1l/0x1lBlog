package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Notification;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code notifications}. Xử lý thêm, truy vấn
 * phân trang, đánh dấu đã đọc và dọn dẹp thông báo cũ.
 */
@Mapper
public interface NotificationRepository {

    @Select("""
        SELECT * FROM notifications WHERE user_id = #{userId}
        ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}
    """)
    List<Notification> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId} AND is_read = FALSE")
    long countUnread(Long userId);

    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId}")
    long countByUserId(Long userId);

    @Insert("""
        INSERT INTO notifications (user_id, actor_id, type, title, message, target_type, target_id)
        VALUES (#{userId}, #{actorId}, #{type}, #{title}, #{message}, #{targetType}, #{targetId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notification notification);

    @Update("UPDATE notifications SET is_read = TRUE WHERE user_id = #{userId} AND is_read = FALSE")
    int markAllRead(Long userId);

    @Update("UPDATE notifications SET is_read = TRUE WHERE id = #{id}")
    int markRead(Long id);

    @Delete("DELETE FROM notifications WHERE created_at < NOW() - INTERVAL '90 days'")
    int deleteOlderThan90Days();
}
