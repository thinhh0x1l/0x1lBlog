package top.blogapi.engagement.bookmark.domain.repository;

import top.blogapi.engagement.bookmark.domain.entity.Bookmark;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    Optional<Bookmark> findById(Long id);

    Optional<Bookmark> findByUserAndBlog(Long userId, Long blogId);

    List<Bookmark> findByUserId(Long userId, int limit, int offset);

    List<Bookmark> findByUserIdAndCollection(Long userId, String collection, int limit, int offset);

    void upsert(Bookmark bookmark);

    void delete(Long userId, Long blogId);

    long countByUserId(Long userId);

    long countByBlogId(Long blogId);
}
