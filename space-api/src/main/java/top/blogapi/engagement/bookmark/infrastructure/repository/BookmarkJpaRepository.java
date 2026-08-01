package top.blogapi.engagement.bookmark.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.bookmark.domain.entity.Bookmark;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndBlogId(Long userId, Long blogId);

    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Bookmark> findByUserIdAndCollectionOrderByCreatedAtDesc(Long userId, String collection, Pageable pageable);

    @Modifying
    @Query(value = """
        INSERT INTO bookmarks (user_id, blog_id, collection, note, is_public)
        VALUES (:userId, :blogId, :collection, :note, :isPublic)
        ON CONFLICT (user_id, blog_id) DO UPDATE SET collection = :collection, note = :note, is_public = :isPublic
        """, nativeQuery = true)
    void upsert(@Param("userId") Long userId, @Param("blogId") Long blogId, @Param("collection") String collection, @Param("note") String note, @Param("isPublic") Boolean isPublic);

    @Modifying
    @Query(value = "DELETE FROM bookmarks WHERE user_id = :userId AND blog_id = :blogId", nativeQuery = true)
    void deleteByUserIdAndBlogId(@Param("userId") Long userId, @Param("blogId") Long blogId);

    long countByUserId(Long userId);

    long countByBlogId(Long blogId);
}
