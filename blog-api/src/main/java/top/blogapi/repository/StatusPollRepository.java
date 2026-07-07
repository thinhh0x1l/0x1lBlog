package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.StatusPoll;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code status_polls}. Quản lý bình chọn gắn với
 * bài trạng thái, bao gồm tùy chọn lưu dưới dạng JSON.
 */
@Mapper
public interface StatusPollRepository {

    @Select("SELECT * FROM status_polls WHERE id = #{id}")
    Optional<StatusPoll> findById(Long id);

    @Select("SELECT * FROM status_polls WHERE status_id = #{statusId}")
    Optional<StatusPoll> findByStatusId(Long statusId);

    @Insert("""
        INSERT INTO status_polls (status_id, question, options, ends_at)
        VALUES (#{statusId}, #{question}, #{options}::jsonb, #{endsAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StatusPoll poll);
}
