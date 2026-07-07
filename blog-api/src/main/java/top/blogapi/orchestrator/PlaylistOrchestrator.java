package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.dto.mapper.PlaylistMapper;
import top.blogapi.dto.request.playlist.AddSongRequest;
import top.blogapi.dto.request.playlist.ReorderRequest;
import top.blogapi.dto.response.PlaylistResponse;
import top.blogapi.dto.response.PlaylistSongResponse;
import top.blogapi.model.entity.Playlist;
import top.blogapi.model.entity.PlaylistSong;
import top.blogapi.service.playlist.PlaylistService;

import java.util.List;

/**
 * Orchestrates playlist management: creation, song add/remove/vote/reorder, with ownership and limit enforcement.
 */
@Component
@RequiredArgsConstructor
public class PlaylistOrchestrator {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Transactional
    public PlaylistResponse getMyPlaylist(Long userId) {
        Playlist playlist = playlistService.findOrCreateByOwnerId(userId);
        List<PlaylistSong> songs = playlistService.getSongsByPlaylistId(playlist.getId());
        return buildResponse(playlist, songs);
    }

    public PlaylistResponse getUserPlaylist(Long userId, Long targetUserId) {
        Playlist playlist = playlistService.findOrCreateByOwnerId(targetUserId);
        if (!playlist.getIsPublic() && !userId.equals(targetUserId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "This playlist is private");
        }
        List<PlaylistSong> songs = playlistService.getSongsByPlaylistId(playlist.getId());
        return buildResponse(playlist, songs);
    }

    @Transactional
    public PlaylistSongResponse addSong(Long userId, AddSongRequest request) {
        Playlist playlist = playlistService.findOrCreateByOwnerId(userId);

        int songCount = playlistService.countSongsByPlaylistId(playlist.getId());
        if (songCount >= 50) {
            throw new AppException(ErrorCode.PLAYLIST_SONG_LIMIT);
        }

        int dailyCount = playlistService.countTodaySongsByUserAndPlaylist(playlist.getId(), userId);
        if (dailyCount >= 5) {
            throw new AppException(ErrorCode.PLAYLIST_DAILY_LIMIT);
        }

        if (playlistService.findSongByPlaylistAndSource(playlist.getId(), request.getSource(), request.getSourceId()).isPresent()) {
            throw new AppException(ErrorCode.PLAYLIST_SONG_ALREADY_EXISTS);
        }

        PlaylistSong song = new PlaylistSong();
        song.setPlaylistId(playlist.getId());
        song.setAddedBy(userId);
        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        song.setSource(request.getSource());
        song.setSourceId(request.getSourceId());
        song.setThumbnailUrl(request.getThumbnailUrl());
        song.setDurationSec(request.getDurationSec());

        song = playlistService.addSong(song);
        return playlistMapper.toSongResponse(song);
    }

    @Transactional
    public void removeSong(Long userId, Long songId) {
        PlaylistSong song = playlistService.findSongById(songId);
        Playlist playlist = playlistService.findById(song.getPlaylistId());
        if (!playlist.getOwnerId().equals(userId)) {
            throw new AppException(ErrorCode.PLAYLIST_NOT_OWNER);
        }
        playlistService.removeSong(songId);
    }

    @Transactional
    public PlaylistSongResponse voteSong(Long userId, Long songId, int vote) {
        PlaylistSong song = playlistService.voteSong(songId, userId, vote);
        return playlistMapper.toSongResponse(song);
    }

    @Transactional
    public void reorderSongs(Long userId, ReorderRequest request) {
        if (request.getSongIds() == null || request.getSongIds().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Song IDs must not be empty");
        }

        PlaylistSong firstSong = playlistService.findSongById(request.getSongIds().get(0));
        Playlist playlist = playlistService.findById(firstSong.getPlaylistId());
        if (!playlist.getOwnerId().equals(userId)) {
            throw new AppException(ErrorCode.PLAYLIST_NOT_OWNER);
        }
        playlistService.reorderSongs(playlist.getId(), request.getSongIds());
    }

    private PlaylistResponse buildResponse(Playlist playlist, List<PlaylistSong> songs) {
        PlaylistResponse response = playlistMapper.toResponse(playlist);
        List<PlaylistSongResponse> songResponses = songs.stream()
                .map(playlistMapper::toSongResponse)
                .toList();
        response.setSongs(songResponses);
        return response;
    }
}
