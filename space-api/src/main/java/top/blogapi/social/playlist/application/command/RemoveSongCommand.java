package top.blogapi.social.playlist.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.service.PlaylistService;

@Service
@RequiredArgsConstructor
public class RemoveSongCommand {

    private final PlaylistService playlistService;

    @Transactional
    public void execute(Long userId, Long songId) {
        PlaylistSong song = playlistService.findSongById(songId);
        Playlist playlist = playlistService.findById(song.getPlaylistId());
        if (!playlist.getOwnerId().equals(userId)) {
            throw new AppException(ErrorCode.PLAYLIST_NOT_OWNER);
        }
        playlistService.removeSong(songId);
    }
}
