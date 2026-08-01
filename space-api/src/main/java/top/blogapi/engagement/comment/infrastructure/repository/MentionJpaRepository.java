package top.blogapi.engagement.comment.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.Mention;

import java.util.List;

@Repository
public interface MentionJpaRepository extends JpaRepository<Mention, Long> {

    List<Mention> findByTargetUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
