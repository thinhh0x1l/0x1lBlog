package top.blogapi.social.status.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.status.domain.entity.StatusPoll;
import top.blogapi.social.status.domain.entity.StatusPollVote;
import top.blogapi.social.status.domain.service.StatusService;

@Service
@RequiredArgsConstructor
public class VoteStatusCommand {

    private final StatusService statusService;

    @Transactional
    public StatusPollVote execute(Long statusId, Long userId, Integer optionIndex) {
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
}
