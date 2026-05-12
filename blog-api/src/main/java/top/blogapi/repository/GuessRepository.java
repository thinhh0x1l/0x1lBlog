package top.blogapi.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.blogapi.model.entity.Guess;

import java.util.Optional;

public interface GuessRepository {

    @Select("""
        SELECT * FROM guess WHERE token_hash = #{tokenHash}
""")
    Optional<Guess> getGuessByTokenHash(String tokenHash);

    @Insert("""
        INSERT INTO guess (
            token_hash,
            create_at,
            last_seen_at
        )values(
            #{tokenHash},
            #{createAt},
            #{lastSeenAt}
        )
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addGuess (Guess guess);

    @Select("""
        SELECT id FROM guess WHERE token_hash = #{tokenHash}
""")
    Long getGuessIdByTokenHash(String tokenHash);
}
