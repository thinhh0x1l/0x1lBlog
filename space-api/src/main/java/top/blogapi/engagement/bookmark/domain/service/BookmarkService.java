package top.blogapi.engagement.bookmark.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.engagement.bookmark.domain.entity.Bookmark;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.engagement.bookmark.domain.repository.BookmarkRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BlogRepository blogRepository;

    public List<Bookmark> getByUserId(Long userId, int page, int size) {
        return bookmarkRepository.findByUserId(userId, size, page * size);
    }

    public void toggle(Bookmark bookmark) {
        bookmarkRepository.upsert(bookmark);
    }

    public void remove(Long userId, Long blogId) {
        bookmarkRepository.delete(userId, blogId);
    }

    public boolean isBookmarked(Long userId, Long blogId) {
        return bookmarkRepository.findByUserAndBlog(userId, blogId).isPresent();
    }

    public long countByUserId(Long userId) {
        return bookmarkRepository.countByUserId(userId);
    }

    public void incrementBookmarkCount(Long blogId) {
        blogRepository.incrementBookmarkCount(blogId);
    }

    public void decrementBookmarkCount(Long blogId) {
        blogRepository.decrementBookmarkCount(blogId);
    }
}
