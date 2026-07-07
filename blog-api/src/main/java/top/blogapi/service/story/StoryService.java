package top.blogapi.service.story;

import top.blogapi.model.entity.Story;
import top.blogapi.model.entity.StoryArchive;

import java.util.List;

/**
 * Giao diện service cho story tạm thời, hỗ trợ tạo, tự động hết hạn sau 24 giờ,
 * lưu trữ, theo dõi lượt xem và giới hạn sử dụng hàng ngày.
 */
public interface StoryService {

    Story create(Story story);

    Story findById(Long id);

    List<Story> getActiveByUserId(Long userId);

    List<Story> getActiveFeed(int limit);

    void softDelete(Long id, Long userId);

    void incrementViewCount(Long id);

    void archiveExpiredStories();

    long countTodayByUserId(Long userId);

    List<StoryArchive> getArchivedByUserId(Long userId, int page, int size);
}
