package top.blogapi.social.playlist.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.social.playlist.interfaces.dto.PlaylistMapper;
import top.blogapi.social.playlist.interfaces.dto.PlaylistSongResponse;
import top.blogapi.social.playlist.domain.service.PlaylistService;

@Service
@RequiredArgsConstructor
public class VoteSongCommand {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Transactional
    public PlaylistSongResponse execute(Long userId, Long songId, int vote) {
        var song = playlistService.voteSong(songId, userId, vote);
        return playlistMapper.toSongResponse(song);
    }
}
