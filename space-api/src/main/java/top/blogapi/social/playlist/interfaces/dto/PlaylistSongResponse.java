package top.blogapi.social.playlist.interfaces.dto;

import java.time.Instant;

public record PlaylistSongResponse(
        Long id,
        Long playlistId,
        Long addedBy,
        String title,
        String artist,
        String source,
        String sourceId,
        String thumbnailUrl,
        Integer durationSec,
        Integer voteCount,
        Integer sortOrder,
        Instant addedAt
) {}
