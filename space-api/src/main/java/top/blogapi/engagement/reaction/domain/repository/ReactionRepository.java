package top.blogapi.engagement.reaction.domain.repository;

import top.blogapi.engagement.reaction.domain.entity.Reaction;

import java.util.Optional;

public interface ReactionRepository {

    Optional<Reaction> findByUserAndTarget(Long userId, String targetType, Long targetId);

    void upsert(Reaction reaction);

    void delete(Long userId, String targetType, Long targetId);

    int countByTargetAndType(String targetType, Long targetId, String type);

    String findTypeByUserAndTarget(Long userId, String targetType, Long targetId);
}
