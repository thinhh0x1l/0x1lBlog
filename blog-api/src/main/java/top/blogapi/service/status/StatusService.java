package top.blogapi.service.status;

import top.blogapi.model.entity.Status;
import top.blogapi.model.entity.StatusPoll;
import top.blogapi.model.entity.StatusPollVote;

import java.util.List;

/**
 * Giao diện service cho trạng thái và bình chọn, hỗ trợ trạng thái văn bản,
 * hội thoại theo chuỗi, tạo bình chọn, bỏ phiếu và giới hạn tốc độ hàng ngày.
 */
public interface StatusService {
    Status create(Status status);

    Status findById(Long id);

    List<Status> findByUserId(Long userId, int page, int size);

    List<Status> getFeed(int limit);

    List<Status> getThreadParts(Long threadId);

    void softDelete(Long id);

    void checkDailyRateLimit(Long userId);

    StatusPoll createPoll(StatusPoll poll);

    StatusPoll findPollByStatusId(Long statusId);

    StatusPollVote castVote(StatusPollVote vote);

    boolean hasUserVoted(Long pollId, Long userId);

    long countVotesByPollAndOption(Long pollId, Integer optionIndex);

    long countByUserId(Long userId);

    List<StatusPollVote> findVotesByPollId(Long pollId);
}
