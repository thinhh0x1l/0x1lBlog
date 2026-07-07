package top.blogapi.orchestrator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.BadRequestException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.dto.mapper.StatusMapper;
import top.blogapi.dto.request.status.StatusRequest;
import top.blogapi.dto.response.StatusResponse;
import top.blogapi.model.entity.Status;
import top.blogapi.model.entity.StatusPoll;
import top.blogapi.model.entity.StatusPollVote;
import top.blogapi.service.status.StatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Orchestrates status updates: creation with optional polls, feed retrieval, voting, and moderation.
 */
@Component
@RequiredArgsConstructor
public class StatusOrchestrator {

    private final StatusService statusService;
    private final StatusMapper statusMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public Status createStatus(StatusRequest request, Long userId) {
        statusService.checkDailyRateLimit(userId);

        Status status = new Status();
        status.setUserId(userId);
        status.setContent(request.getContent());
        status.setImageUrl(request.getImageUrl());
        status.setVisibility(request.getVisibility() != null ? request.getVisibility() : "PUBLIC");
        status.setThreadId(request.getThreadId());
        status = statusService.create(status);

        if (request.getPoll() != null) {
            if (request.getPoll().getOptions() == null || request.getPoll().getOptions().size() < 2
                    || request.getPoll().getOptions().size() > 6) {
                throw new BadRequestException("Poll must have between 2 and 6 options");
            }
            StatusPoll poll = new StatusPoll();
            poll.setStatusId(status.getId());
            poll.setQuestion(request.getPoll().getQuestion());
            poll.setOptions(toJsonArray(request.getPoll().getOptions()));
            poll.setEndsAt(request.getPoll().getEndsAt());
            statusService.createPoll(poll);
        }

        return status;
    }

    public Status getStatus(Long id) {
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
    public void deleteStatus(Long id) {
        statusService.softDelete(id);
    }

    public StatusPoll getPollByStatusId(Long statusId) {
        return statusService.findPollByStatusId(statusId);
    }

    @Transactional
    public StatusPollVote castVote(Long statusId, Long userId, Integer optionIndex) {
        StatusPoll poll = statusService.findPollByStatusId(statusId);
        if (poll == null) {
            throw new AppException(ErrorCode.STATUS_NOT_FOUND, "No poll attached to this status");
        }
        StatusPollVote vote = new StatusPollVote();
        vote.setPollId(poll.getId());
        vote.setUserId(userId);
        vote.setOptionIndex(optionIndex);
        return statusService.castVote(vote);
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
        var response = statusMapper.toResponse(status);
        var poll = statusService.findPollByStatusId(status.getId());
        if (poll != null) {
            var pollResp = new StatusResponse.PollResponse();
            pollResp.setId(poll.getId());
            pollResp.setQuestion(poll.getQuestion());
            pollResp.setEndsAt(poll.getEndsAt());
            try {
                List<String> options = objectMapper.readValue(poll.getOptions(),
                        new TypeReference<List<String>>() {});
                pollResp.setOptions(options);
            } catch (Exception e) {
                pollResp.setOptions(List.of());
            }
            var votes = statusService.findVotesByPollId(status.getId());
            Map<Integer, Long> voteCounts = votes.stream()
                    .collect(Collectors.groupingBy(StatusPollVote::getOptionIndex, Collectors.counting()));
            Map<Integer, Long> fullCounts = new HashMap<>();
            if (pollResp.getOptions() != null) {
                for (int i = 0; i < pollResp.getOptions().size(); i++) {
                    fullCounts.put(i, voteCounts.getOrDefault(i, 0L));
                }
            }
            pollResp.setVoteCounts(fullCounts);
            pollResp.setVoted(statusService.hasUserVoted(poll.getId(), currentUserId));
            response.setPoll(pollResp);
        }
        return response;
    }

    private String toJsonArray(List<String> options) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < options.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(escapeJson(options.get(i))).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
