package top.blogapi.engagement.comment.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.Mention;
import top.blogapi.engagement.comment.domain.repository.MentionRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MentionRepositoryImpl implements MentionRepository {

    private final MentionJpaRepository jpa;

    @Override
    public void insert(Mention mention) {
        jpa.save(mention);
    }

    @Override
    public List<Mention> findByTargetUserId(Long userId, int limit, int offset) {
        return jpa.findByTargetUserIdOrderByCreatedAtDesc(userId, PageRequest.of(offset / limit, limit));
    }
}
