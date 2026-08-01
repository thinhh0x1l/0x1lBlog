package top.blogapi.social.status.application.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.status.interfaces.dto.StatusMapper;
import top.blogapi.social.status.interfaces.dto.StatusResponse;
import top.blogapi.social.status.domain.entity.Status;
import top.blogapi.social.status.domain.entity.StatusPoll;
import top.blogapi.social.status.domain.entity.StatusPollVote;
import top.blogapi.social.status.domain.service.StatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetStatusQuery {

    private final StatusService statusService;
    private final StatusMapper statusMapper;
    private final ObjectMapper objectMapper;

    public Status execute(Long id) {
        return statusService.findById(id);
    }

    public long countByUserId(Long userId) {
        return statusService.countByUserId(userId);
    }

    public List<Status> getByUser(Long userId, int page, int size) {
        return statusService.findByUserId(userId, page, size);
    }

    public List<Status> getFeed(int limit) {
        return statusService.getFeed(limit);
    }

    public List<Status> getThreadParts(Long threadId) {
        return statusService.getThreadParts(threadId);
    }

    @Transactional
    public void deleteStatus(Long id, Long userId) {
        var status = statusService.findById(id);
        if (!status.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền xóa status này");
        }
        statusService.softDelete(id);
    }

    public StatusPoll getPollByStatusId(Long statusId) {
        return statusService.findPollByStatusId(statusId);
    }

    public boolean hasUserVotedOnPoll(Long statusId, Long userId) {
        StatusPoll poll = statusService.findPollByStatusId(statusId);
        if (poll == null) {
            return false;
        }
        return statusService.hasUserVoted(poll.getId(), userId);
    }

    public List<StatusPollVote> getPollVotes(Long statusId) {
        StatusPoll poll = statusService.findPollByStatusId(statusId);
        if (poll == null) {
            return List.of();
        }
        return statusService.findVotesByPollId(poll.getId());
    }

    public StatusResponse buildStatusResponse(Status status, Long currentUserId) {
        var poll = statusService.findPollByStatusId(status.getId());
        StatusResponse.PollResponse pollResp = null;
        if (poll != null) {
            List<String> options;
            try {
                options = objectMapper.readValue(poll.getOptions(),
                        new TypeReference<List<String>>() {});
            } catch (Exception e) {
                options = List.of();
            }
            var votes = statusService.findVotesByPollId(status.getId());
            Map<Integer, Long> voteCounts = votes.stream()
                    .collect(Collectors.groupingBy(StatusPollVote::getOptionIndex, Collectors.counting()));
            Map<Integer, Long> fullCounts = new HashMap<>();
            if (options != null) {
                for (int i = 0; i < options.size(); i++) {
                    fullCounts.put(i, voteCounts.getOrDefault(i, 0L));
                }
            }
            pollResp = new StatusResponse.PollResponse(
                    poll.getId(),
                    poll.getQuestion(),
                    poll.getEndsAt(),
                    options,
                    fullCounts,
                    statusService.hasUserVoted(poll.getId(), currentUserId)
            );
        }
        return new StatusResponse(
                status.getId(),
                status.getUserId(),
                status.getThreadId(),
                status.getPartOrder(),
                status.getContent(),
                status.getImageUrl(),
                status.getVisibility(),
                status.getCreatedAt(),
                pollResp
        );
    }
}
