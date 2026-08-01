package top.blogapi.gamification.blind.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;

import java.util.List;

@Mapper
public interface BlindChallengeGuessMybatisMapper {

    @Select("SELECT bg.* FROM blind_challenge_guesses bg JOIN users u ON u.id = bg.user_id WHERE bg.challenge_id = #{challengeId} AND bg.is_correct = TRUE ORDER BY bg.created_at")
    List<BlindChallengeGuess> findLeaderboard(Long challengeId);
}
