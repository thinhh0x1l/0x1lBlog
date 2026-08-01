package top.blogapi.engagement.reaction.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.reaction.domain.entity.Reaction;

import java.util.Optional;

@Repository
public interface ReactionJpaRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByUserIdAndTargetTypeAndTargetId(Long userId, String targetType, Long targetId);

    @Modifying
    @Query(value = """
        INSERT INTO reactions (user_id, target_type, target_id, type)
        VALUES (:userId, :targetType, :targetId, :type)
        ON CONFLICT (user_id, target_type, target_id) DO UPDATE SET type = :type
        """, nativeQuery = true)
    void upsert(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("type") String type);

    @Modifying
    @Query(value = "DELETE FROM reactions WHERE user_id = :userId AND target_type = :targetType AND target_id = :targetId", nativeQuery = true)
    void deleteByUserIdAndTargetTypeAndTargetId(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Query(value = "SELECT COUNT(*) FROM reactions WHERE target_type = :targetType AND target_id = :targetId AND type = :type", nativeQuery = true)
    int countByTargetTypeAndTargetIdAndType(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("type") String type);

    @Query(value = "SELECT type FROM reactions WHERE user_id = :userId AND target_type = :targetType AND target_id = :targetId", nativeQuery = true)
    String findTypeByUserIdAndTargetTypeAndTargetId(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);
}
