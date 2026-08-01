package top.blogapi.social.status.interfaces.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record StatusResponse(
        Long id,
        Long userId,
        Long threadId,
        Integer partOrder,
        String content,
        String imageUrl,
        String visibility,
        Instant createdAt,
        PollResponse poll
) {
    public record PollResponse(
            Long id,
            String question,
            Instant endsAt,
            List<String> options,
            Map<Integer, Long> voteCounts,
            boolean voted
    ) {}
}
