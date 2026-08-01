package top.blogapi.content.blog.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.blog.domain.entity.BlogHashtag;

import java.util.List;

@Repository
public interface BlogHashtagJpaRepository extends JpaRepository<BlogHashtag, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO blog_hashtags (blog_id, hashtag_id)
        SELECT :#{#bh.blogId}, :#{#bh.hashtagId}
        WHERE NOT EXISTS (
            SELECT 1 FROM blog_hashtags WHERE blog_id = :#{#bh.blogId} AND hashtag_id = :#{#bh.hashtagId}
        )
    """, nativeQuery = true)
    void saveIfNotExists(@Param("bh") BlogHashtag blogHashtag);

    @Modifying
    @Query(value = "DELETE FROM blog_hashtags WHERE blog_id = :blogId AND hashtag_id = :hashtagId", nativeQuery = true)
    void deleteByBlogIdAndHashtagId(@Param("blogId") Long blogId, @Param("hashtagId") Long hashtagId);

    @Modifying
    @Query(value = "DELETE FROM blog_hashtags WHERE blog_id = :blogId", nativeQuery = true)
    void deleteByBlogId(@Param("blogId") Long blogId);

    List<BlogHashtag> findByBlogId(Long blogId);
}
