package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.StatusPollVote;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code status_poll_votes}. Theo dõi từng phiếu
 * bầu trong bình chọn trạng thái với đếm theo tùy chọn.
 */
@Mapper
public interface StatusPollVoteRepository {

    @Select("SELECT * FROM status_poll_votes WHERE id = #{id}")
    Optional<StatusPollVote> findById(Long id);

    @Select("SELECT * FROM status_poll_votes WHERE poll_id = #{pollId}")
    List<StatusPollVote> findByPollId(Long pollId);

    @Select("SELECT * FROM status_poll_votes WHERE poll_id = #{pollId} AND user_id = #{userId}")
    Optional<StatusPollVote> findByPollAndUser(@Param("pollId") Long pollId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM status_poll_votes WHERE poll_id = #{pollId} AND option_index = #{optionIndex}")
    long countByPollAndOption(@Param("pollId") Long pollId, @Param("optionIndex") Integer optionIndex);

    @Insert("""
        INSERT INTO status_poll_votes (poll_id, user_id, option_index)
        VALUES (#{pollId}, #{userId}, #{optionIndex})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StatusPollVote vote);

    @Delete("DELETE FROM status_poll_votes WHERE poll_id = #{pollId} AND user_id = #{userId}")
    int delete(@Param("pollId") Long pollId, @Param("userId") Long userId);
}
