package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.blind.BlindChallenge;

import java.time.LocalDate;
import java.util.Optional;

/**
 * MyBatis mapper for the {@code blind_challenges} table. Manages daily
 * blind challenges with topic hints, options, and reveal logic.
 */
@Mapper
public interface BlindChallengeRepository {

    @Select("SELECT * FROM blind_challenges WHERE date = #{date}")
    Optional<BlindChallenge> findByDate(LocalDate date);

    @Select("SELECT * FROM blind_challenges ORDER BY date DESC LIMIT 1")
    Optional<BlindChallenge> findTopByOrderByDateDesc();

    @Insert("""
        INSERT INTO blind_challenges (date, topic_id, topic_hint, options, revealed)
        VALUES (#{date}, #{topicId}, #{topicHint}, #{options}::jsonb, #{isRevealed})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlindChallenge challenge);

    @Update("UPDATE blind_challenges SET revealed = TRUE WHERE id = #{id}")
    int reveal(Long id);
}
