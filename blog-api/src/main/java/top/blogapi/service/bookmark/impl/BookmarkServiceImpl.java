package top.blogapi.service.bookmark.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.Bookmark;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.BookmarkRepository;
import top.blogapi.service.bookmark.BookmarkService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai BookmarkService với cơ chế đánh dấu dạng chuyển đổi
 * và đồng bộ bộ đếm dấu trang blog.
 */
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BlogRepository blogRepository;

    @Override
    public List<Bookmark> getByUserId(Long userId, int page, int size) {
        return bookmarkRepository.findByUserId(userId, size, page * size);
    }

    @Override
    public void toggle(Bookmark bookmark) {
        bookmarkRepository.upsert(bookmark);
    }

    @Override
    public void remove(Long userId, Long blogId) {
        bookmarkRepository.delete(userId, blogId);
    }

    @Override
    public boolean isBookmarked(Long userId, Long blogId) {
        return bookmarkRepository.findByUserAndBlog(userId, blogId).isPresent();
    }

    @Override
    public long countByUserId(Long userId) {
        return bookmarkRepository.countByUserId(userId);
    }

    @Override
    public void incrementBookmarkCount(Long blogId) {
        blogRepository.incrementBookmarkCount(blogId);
    }

    @Override
    public void decrementBookmarkCount(Long blogId) {
        blogRepository.decrementBookmarkCount(blogId);
    }
}
