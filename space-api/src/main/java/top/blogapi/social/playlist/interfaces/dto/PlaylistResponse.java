package top.blogapi.social.playlist.interfaces.dto;

import java.time.Instant;
import java.util.List;

public record PlaylistResponse(
        Long id,
        Long ownerId,
        String title,
        Boolean isPublic,
        Integer songCount,
        Instant createdAt,
        List<PlaylistSongResponse> songs
) {}
