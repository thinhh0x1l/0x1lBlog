package top.blogapi.engagement.share.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.share.domain.entity.Share;
import top.blogapi.engagement.share.domain.repository.ShareRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShareRepositoryImpl implements ShareRepository {

    private final ShareJpaRepository jpa;

    @Override
    public void insert(Share share) {
        jpa.save(share);
    }

    @Override
    public List<Share> findByTarget(String targetType, Long targetId, int limit, int offset) {
        return jpa.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId, PageRequest.of(offset / limit, limit));
    }

    @Override
    public long countByTarget(String targetType, Long targetId) {
        return jpa.countByTargetTypeAndTargetId(targetType, targetId);
    }
}
