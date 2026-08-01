package top.blogapi.engagement.reaction.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.reaction.domain.entity.BlogReaction;

import java.util.Optional;

@Repository
public interface BlogReactionJpaRepository extends JpaRepository<BlogReaction, Long> {

    Optional<BlogReaction> findByUserIdAndBlogId(Long userId, Long blogId);

    @Modifying
    @Query(value = """
        INSERT INTO blog_reactions (user_id, blog_id, type)
        VALUES (:userId, :blogId, :type)
        ON CONFLICT (user_id, blog_id) DO UPDATE SET type = :type
        """, nativeQuery = true)
    void upsert(@Param("userId") Long userId, @Param("blogId") Long blogId, @Param("type") String type);

    @Modifying
    @Query(value = "DELETE FROM blog_reactions WHERE user_id = :userId AND blog_id = :blogId", nativeQuery = true)
    void deleteByUserIdAndBlogId(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Query(value = "SELECT COUNT(*) FROM blog_reactions WHERE blog_id = :blogId AND type = :type", nativeQuery = true)
    int countByBlogIdAndType(@Param("blogId") Long blogId, @Param("type") String type);

    @Query(value = "SELECT type FROM blog_reactions WHERE user_id = :userId AND blog_id = :blogId", nativeQuery = true)
    String findTypeByUserIdAndBlogId(@Param("userId") Long userId, @Param("blogId") Long blogId);
}
