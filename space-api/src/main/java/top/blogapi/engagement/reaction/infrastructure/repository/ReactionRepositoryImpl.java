package top.blogapi.engagement.reaction.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.reaction.domain.entity.Reaction;
import top.blogapi.engagement.reaction.domain.repository.ReactionRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReactionRepositoryImpl implements ReactionRepository {

    private final ReactionJpaRepository jpa;

    @Override
    public Optional<Reaction> findByUserAndTarget(Long userId, String targetType, Long targetId) {
        return jpa.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }

    @Override
    public void upsert(Reaction reaction) {
        jpa.upsert(reaction.getUserId(), reaction.getTargetType(), reaction.getTargetId(), reaction.getType());
    }

    @Override
    public void delete(Long userId, String targetType, Long targetId) {
        jpa.deleteByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }

    @Override
    public int countByTargetAndType(String targetType, Long targetId, String type) {
        return jpa.countByTargetTypeAndTargetIdAndType(targetType, targetId, type);
    }

    @Override
    public String findTypeByUserAndTarget(Long userId, String targetType, Long targetId) {
        return jpa.findTypeByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }
}
