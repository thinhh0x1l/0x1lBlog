package top.blogapi.engagement.comment.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.CommentReaction;

import java.util.Optional;

@Repository
public interface CommentReactionJpaRepository extends JpaRepository<CommentReaction, Long> {

    Optional<CommentReaction> findByUserIdAndCommentId(Long userId, Long commentId);

    @Modifying
    @Query(value = "DELETE FROM comment_reactions WHERE user_id = :userId AND comment_id = :commentId", nativeQuery = true)
    void deleteByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    long countByCommentId(Long commentId);
}
