package top.blogapi.social.playlist.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.playlist.interfaces.dto.PlaylistMapper;
import top.blogapi.social.playlist.interfaces.dto.AddSongRequest;
import top.blogapi.social.playlist.interfaces.dto.PlaylistSongResponse;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.service.PlaylistService;

@Service
@RequiredArgsConstructor
public class AddSongCommand {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Transactional
    public PlaylistSongResponse execute(Long userId, AddSongRequest request) {
        Playlist playlist = playlistService.findOrCreateByOwnerId(userId);

        int songCount = playlistService.countSongsByPlaylistId(playlist.getId());
        if (songCount >= 50) {
            throw new AppException(ErrorCode.PLAYLIST_SONG_LIMIT);
        }

        int dailyCount = playlistService.countTodaySongsByUserAndPlaylist(playlist.getId(), userId);
        if (dailyCount >= 5) {
            throw new AppException(ErrorCode.PLAYLIST_DAILY_LIMIT);
        }

        if (playlistService.findSongByPlaylistAndSource(playlist.getId(), request.source(), request.sourceId()).isPresent()) {
            throw new AppException(ErrorCode.PLAYLIST_SONG_ALREADY_EXISTS);
        }

        PlaylistSong song = new PlaylistSong();
        song.setPlaylistId(playlist.getId());
        song.setAddedBy(userId);
        song.setTitle(request.title());
        song.setArtist(request.artist());
        song.setSource(request.source());
        song.setSourceId(request.sourceId());
        song.setThumbnailUrl(request.thumbnailUrl());
        song.setDurationSec(request.durationSec());

        song = playlistService.addSong(song);
        return playlistMapper.toSongResponse(song);
    }
}
