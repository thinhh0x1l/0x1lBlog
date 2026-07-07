package top.blogapi.service.bookmark;

import top.blogapi.model.entity.Bookmark;

import java.util.List;

/**
 * Giao diện service quản lý dấu trang, cho phép người dùng
 * đánh dấu blog và chuyển đổi trạng thái.
 */
public interface BookmarkService {
    List<Bookmark> getByUserId(Long userId, int page, int size);
    void toggle(Bookmark bookmark);
    void remove(Long userId, Long blogId);
    boolean isBookmarked(Long userId, Long blogId);
    long countByUserId(Long userId);

    void incrementBookmarkCount(Long blogId);
    void decrementBookmarkCount(Long blogId);
}
