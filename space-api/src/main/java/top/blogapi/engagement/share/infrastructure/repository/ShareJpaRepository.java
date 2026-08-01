package top.blogapi.engagement.share.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.share.domain.entity.Share;

import java.util.List;

@Repository
public interface ShareJpaRepository extends JpaRepository<Share, Long> {

    List<Share> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(String targetType, Long targetId, Pageable pageable);

    long countByTargetTypeAndTargetId(String targetType, Long targetId);
}
