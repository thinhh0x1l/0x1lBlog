package top.blogapi.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.blogapi.model.entity.Guest;

import java.util.Optional;

public interface GuestRepository {

    @Select("""
        SELECT * FROM guest WHERE token_hash = #{tokenHash}
""")
    Optional<Guest> getGuessByTokenHash(String tokenHash);

    @Insert("""
    INSERT INTO guest (
        token_hash,
        create_at,
        last_seen_at
    )
    VALUES (
        #{tokenHash},
        #{createAt},
        #{lastSeenAt}
    )
    ON DUPLICATE KEY UPDATE
    last_seen_at = VALUES(last_seen_at)
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addGuest(Guest guest);

    @Select("""
        SELECT id FROM guest WHERE token_hash = #{tokenHash}
""")
    Long getGuessIdByTokenHash(String tokenHash);
}
