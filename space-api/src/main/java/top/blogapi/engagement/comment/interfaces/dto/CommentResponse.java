package top.blogapi.engagement.comment.interfaces.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String targetType,
        Long targetId,
        Long parentId,
        Long userId,
        String authorName,
        String authorAvatar,
        String content,
        String status,
        Integer replyCount,
        Instant createdAt
) {}
