package top.blogapi.engagement.comment.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.comment.domain.entity.Comment;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "UPDATE comments SET content = :content, updated_at = NOW() WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    void updateContent(@Param("id") Long id, @Param("content") String content);

    @Modifying
    @Query(value = "UPDATE comments SET status = :status WHERE id = :id", nativeQuery = true)
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE comments SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);
}
