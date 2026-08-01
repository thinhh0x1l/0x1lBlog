package top.blogapi.engagement.bookmark.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.bookmark.domain.entity.Bookmark;
import top.blogapi.engagement.bookmark.domain.service.BookmarkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToggleBookmarkCommand {

    private final BookmarkService bookmarkService;

    @Transactional
    public void toggle(Bookmark bookmark) {
        boolean wasBookmarked = bookmarkService.isBookmarked(bookmark.getUserId(), bookmark.getBlogId());
        if (wasBookmarked) {
            bookmarkService.remove(bookmark.getUserId(), bookmark.getBlogId());
            bookmarkService.decrementBookmarkCount(bookmark.getBlogId());
        } else {
            bookmarkService.toggle(bookmark);
            bookmarkService.incrementBookmarkCount(bookmark.getBlogId());
        }
    }

    @Transactional
    public void remove(Long userId, Long blogId) {
        bookmarkService.remove(userId, blogId);
    }

    public List<Bookmark> getByUserId(Long userId, int page, int size) {
        return bookmarkService.getByUserId(userId, page, size);
    }

    public boolean isBookmarked(Long userId, Long blogId) {
        return bookmarkService.isBookmarked(userId, blogId);
    }
}
