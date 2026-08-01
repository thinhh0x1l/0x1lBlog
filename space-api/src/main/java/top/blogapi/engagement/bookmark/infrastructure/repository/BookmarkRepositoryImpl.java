package top.blogapi.engagement.bookmark.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.engagement.bookmark.domain.entity.Bookmark;
import top.blogapi.engagement.bookmark.domain.repository.BookmarkRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository jpa;

    @Override
    public Optional<Bookmark> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Bookmark> findByUserAndBlog(Long userId, Long blogId) {
        return jpa.findByUserIdAndBlogId(userId, blogId);
    }

    @Override
    public List<Bookmark> findByUserId(Long userId, int limit, int offset) {
        return jpa.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(offset / limit, limit));
    }

    @Override
    public List<Bookmark> findByUserIdAndCollection(Long userId, String collection, int limit, int offset) {
        return jpa.findByUserIdAndCollectionOrderByCreatedAtDesc(userId, collection, PageRequest.of(offset / limit, limit));
    }

    @Override
    public void upsert(Bookmark bookmark) {
        jpa.upsert(bookmark.getUserId(), bookmark.getBlogId(), bookmark.getCollection(), bookmark.getNote(), bookmark.getIsPublic());
    }

    @Override
    public void delete(Long userId, Long blogId) {
        jpa.deleteByUserIdAndBlogId(userId, blogId);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpa.countByUserId(userId);
    }

    @Override
    public long countByBlogId(Long blogId) {
        return jpa.countByBlogId(blogId);
    }
}
