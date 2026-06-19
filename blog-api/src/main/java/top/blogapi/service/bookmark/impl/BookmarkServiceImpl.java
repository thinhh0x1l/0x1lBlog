package top.blogapi.service.bookmark.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Bookmark;
import top.blogapi.repository.BookmarkRepository;
import top.blogapi.service.bookmark.BookmarkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public List<Bookmark> getByUserId(Long userId, int page, int size) {
        return bookmarkRepository.findByUserId(userId, size, page * size);
    }

    @Override
    @Transactional
    public void toggle(Bookmark bookmark) {
        bookmarkRepository.upsert(bookmark);
    }

    @Override
    @Transactional
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
}
