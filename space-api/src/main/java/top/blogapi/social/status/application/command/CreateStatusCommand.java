package top.blogapi.social.status.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.status.interfaces.dto.StatusRequest;
import top.blogapi.social.status.domain.entity.Status;
import top.blogapi.social.status.domain.entity.StatusPoll;
import top.blogapi.social.status.domain.service.StatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateStatusCommand {

    private final StatusService statusService;

    @Transactional
    public Status execute(StatusRequest request, Long userId) {
        statusService.checkDailyRateLimit(userId);

        Status status = new Status();
        status.setUserId(userId);
        status.setContent(request.content());
        status.setImageUrl(request.imageUrl());
        status.setVisibility(request.visibility() != null ? request.visibility() : "PUBLIC");
        status.setThreadId(request.threadId());
        status = statusService.create(status);

        if (request.poll() != null) {
            if (request.poll().options() == null || request.poll().options().size() < 2
                    || request.poll().options().size() > 6) {
                throw new AppException(ErrorCode.INVALID_INPUT, "Poll must have between 2 and 6 options");
            }
            StatusPoll poll = new StatusPoll();
            poll.setStatusId(status.getId());
            poll.setQuestion(request.poll().question());
            poll.setOptions(toJsonArray(request.poll().options()));
            poll.setEndsAt(request.poll().endsAt());
            statusService.createPoll(poll);
        }

        return status;
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
