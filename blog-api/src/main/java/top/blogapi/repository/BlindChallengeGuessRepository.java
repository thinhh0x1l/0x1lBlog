package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.blind.BlindChallengeGuess;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper for the {@code blind_challenge_guesses} table. Tracks
 * user guesses in blind challenges with correctness and leaderboard support.
 */
@Mapper
public interface BlindChallengeGuessRepository {

    @Select("SELECT * FROM blind_challenge_guesses WHERE challenge_id = #{challengeId} AND user_id = #{userId}")
    Optional<BlindChallengeGuess> findByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @Select("SELECT EXISTS(SELECT 1 FROM blind_challenge_guesses WHERE challenge_id = #{challengeId} AND user_id = #{userId})")
    boolean existsByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @Select("SELECT * FROM blind_challenge_guesses WHERE challenge_id = #{challengeId} AND is_correct = TRUE")
    List<BlindChallengeGuess> findByChallengeIdAndIsCorrectTrue(Long challengeId);

    @Insert("""
        INSERT INTO blind_challenge_guesses (challenge_id, user_id, guessed_topic_id, is_correct)
        VALUES (#{challengeId}, #{userId}, #{guessedTopicId}, #{isCorrect})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BlindChallengeGuess guess);

    @Update("UPDATE blind_challenge_guesses SET is_correct = #{isCorrect} WHERE challenge_id = #{challengeId} AND user_id = #{userId}")
    int markCorrect(@Param("challengeId") Long challengeId, @Param("userId") Long userId, @Param("isCorrect") Boolean isCorrect);

    @Select("SELECT bg.* FROM blind_challenge_guesses bg JOIN users u ON u.id = bg.user_id WHERE bg.challenge_id = #{challengeId} AND bg.is_correct = TRUE ORDER BY bg.created_at")
    List<BlindChallengeGuess> findLeaderboard(Long challengeId);

    @Select("SELECT * FROM blind_challenge_guesses WHERE challenge_id = #{challengeId}")
    List<BlindChallengeGuess> findByChallengeId(Long challengeId);
}
