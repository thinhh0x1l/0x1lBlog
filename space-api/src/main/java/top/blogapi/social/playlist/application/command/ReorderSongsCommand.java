package top.blogapi.social.playlist.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.playlist.interfaces.dto.ReorderRequest;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.service.PlaylistService;

@Service
@RequiredArgsConstructor
public class ReorderSongsCommand {

    private final PlaylistService playlistService;

    @Transactional
    public void execute(Long userId, ReorderRequest request) {
        if (request.songIds() == null || request.songIds().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Song IDs must not be empty");
        }

        PlaylistSong firstSong = playlistService.findSongById(request.songIds().get(0));
        Playlist playlist = playlistService.findById(firstSong.getPlaylistId());
        if (!playlist.getOwnerId().equals(userId)) {
            throw new AppException(ErrorCode.PLAYLIST_NOT_OWNER);
        }
        playlistService.reorderSongs(playlist.getId(), request.songIds());
    }
}
