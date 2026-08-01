package top.blogapi.content.blog.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.blog.domain.entity.Blog;

@Repository
public interface BlogJpaRepository extends JpaRepository<Blog, Long> {

    @Modifying
    @Query(value = "UPDATE blogs SET status = :status, published_at = CASE WHEN :status = 'PUBLISHED' AND published_at IS NULL THEN NOW() ELSE published_at END WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE blogs SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL", nativeQuery = true)
    long countPublished();

    @Query(value = "SELECT COUNT(*) FROM blogs WHERE author_id = :authorId AND deleted_at IS NULL", nativeQuery = true)
    long countByAuthorId(@Param("authorId") Long authorId);

    @Modifying
    @Query(value = "UPDATE blogs SET is_top = :isTop WHERE id = :id", nativeQuery = true)
    void toggleTop(@Param("id") Long id, @Param("isTop") boolean isTop);

    @Modifying
    @Query(value = "UPDATE blogs SET is_recommend = :isRecommend WHERE id = :id", nativeQuery = true)
    void toggleRecommend(@Param("id") Long id, @Param("isRecommend") boolean isRecommend);

    @Modifying
    @Query(value = "UPDATE blogs SET views = views + 1 WHERE id = :id", nativeQuery = true)
    void incrementViews(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE blogs SET comment_count = comment_count + 1 WHERE id = :id", nativeQuery = true)
    void incrementCommentCount(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE blogs SET bookmark_count = bookmark_count + 1 WHERE id = :id", nativeQuery = true)
    void incrementBookmarkCount(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE blogs SET bookmark_count = GREATEST(0, bookmark_count - 1) WHERE id = :id", nativeQuery = true)
    void decrementBookmarkCount(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE blogs SET views = views + :count WHERE id = :id", nativeQuery = true)
    void incrementViewsBy(@Param("id") Long id, @Param("count") long count);

    @Query(value = "SELECT COUNT(*) FROM blogs WHERE status = 'PUBLISHED' AND deleted_at IS NULL AND (title ILIKE '%' || :keyword || '%' OR content ILIKE '%' || :keyword || '%')", nativeQuery = true)
    long countSearch(@Param("keyword") String keyword);
}
