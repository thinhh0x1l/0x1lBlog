package top.blogapi.service.status.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Status;
import top.blogapi.model.entity.StatusPoll;
import top.blogapi.model.entity.StatusPollVote;
import top.blogapi.model.event.StatusCreatedEvent;
import top.blogapi.repository.StatusPollRepository;
import top.blogapi.repository.StatusPollVoteRepository;
import top.blogapi.repository.StatusRepository;
import top.blogapi.service.status.StatusService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai StatusService xử lý tạo trạng thái với giới hạn 500 ký tự,
 * hội thoại theo chuỗi, xác thực bình chọn (2-6 tùy chọn) và giới hạn tốc độ hàng ngày.
 */
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusPollRepository statusPollRepository;
    private final StatusPollVoteRepository statusPollVoteRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public Status create(Status status) {
        if (status.getContent() != null && status.getContent().length() > 500) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Content exceeds the maximum length of 500 characters");
        }
        if (status.getThreadId() != null) {
            long partCount = countThreadParts(status.getThreadId());
            if (partCount >= 10) {
                throw new AppException(ErrorCode.INVALID_INPUT, "Thread has reached the maximum of 10 parts");
            }
            status.setPartOrder((int) partCount + 1);
        } else {
            status.setPartOrder(0);
        }
        status.markCreated();
        statusRepository.insert(status);
        eventPublisher.publishEvent(new StatusCreatedEvent(status));
        return status;
    }

    @Override
    public Status findById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND));
    }

    @Override
    public List<Status> findByUserId(Long userId, int page, int size) {
        return statusRepository.findByUserId(userId, size, page * size);
    }

    @Override
    public List<Status> getFeed(int limit) {
        return statusRepository.findFeed(limit);
    }

    @Override
    public List<Status> getThreadParts(Long threadId) {
        return statusRepository.findThreadParts(threadId);
    }

    @Override
    public void softDelete(Long id) {
        Status status = findById(id);
        statusRepository.softDelete(id);
    }

    @Override
    public void checkDailyRateLimit(Long userId) {
        long count = statusRepository.countTodayByUserId(userId);
        if (count >= 20) {
            throw new AppException(ErrorCode.RATE_LIMIT_EXCEEDED, "Daily status limit exceeded (max 20 per day)");
        }
    }

    @Override
    public StatusPoll createPoll(StatusPoll poll) {
        if (poll.getQuestion() != null && poll.getQuestion().length() > 200) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Poll question exceeds the maximum length of 200 characters");
        }
        validatePollOptions(poll.getOptions());
        statusPollRepository.insert(poll);
        return poll;
    }

    @Override
    public StatusPoll findPollByStatusId(Long statusId) {
        return statusPollRepository.findByStatusId(statusId).orElse(null);
    }

    @Override
    public StatusPollVote castVote(StatusPollVote vote) {
        StatusPoll poll = statusPollRepository.findById(vote.getPollId())
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_FOUND, "Poll not found"));
        statusPollVoteRepository.findByPollAndUser(vote.getPollId(), vote.getUserId())
                .ifPresent(v -> {
                    throw new AppException(ErrorCode.INVALID_INPUT, "User has already voted on this poll");
                });
        statusPollVoteRepository.insert(vote);
        return vote;
    }

    @Override
    public boolean hasUserVoted(Long pollId, Long userId) {
        return statusPollVoteRepository.findByPollAndUser(pollId, userId).isPresent();
    }

    @Override
    public long countByUserId(Long userId) {
        return statusRepository.countByUserId(userId);
    }

    @Override
    public long countVotesByPollAndOption(Long pollId, Integer optionIndex) {
        return statusPollVoteRepository.countByPollAndOption(pollId, optionIndex);
    }

    @Override
    public List<StatusPollVote> findVotesByPollId(Long pollId) {
        return statusPollVoteRepository.findByPollId(pollId);
    }

    private String toJson(List<String> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Invalid poll options format");
        }
    }

    private void validatePollOptions(String optionsJson) {
        try {
            List<String> options = objectMapper.readValue(optionsJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            if (options.size() < 2 || options.size() > 6) {
                throw new AppException(ErrorCode.INVALID_INPUT, "Poll must have between 2 and 6 options");
            }
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Invalid poll options format");
        }
    }

    private long countThreadParts(Long threadId) {
        return statusRepository.findThreadParts(threadId).size();
    }
}
