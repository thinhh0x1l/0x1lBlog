package top.blogapi.social.playlist.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.playlist.interfaces.dto.PlaylistMapper;
import top.blogapi.social.playlist.interfaces.dto.PlaylistResponse;
import top.blogapi.social.playlist.interfaces.dto.PlaylistSongResponse;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.service.PlaylistService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPlaylistQuery {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

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

    private PlaylistResponse buildResponse(Playlist playlist, List<PlaylistSong> songs) {
        List<PlaylistSongResponse> songResponses = songs.stream()
                .map(playlistMapper::toSongResponse)
                .toList();
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getOwnerId(),
                playlist.getTitle(),
                playlist.getIsPublic(),
                songs.size(),
                playlist.getCreatedAt(),
                songResponses
        );
    }
}
