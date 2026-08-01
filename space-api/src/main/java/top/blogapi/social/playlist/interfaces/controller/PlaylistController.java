package top.blogapi.social.playlist.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.social.playlist.interfaces.dto.AddSongRequest;
import top.blogapi.social.playlist.interfaces.dto.ReorderRequest;
import top.blogapi.social.playlist.interfaces.dto.PlaylistResponse;
import top.blogapi.social.playlist.interfaces.dto.PlaylistSongResponse;
import top.blogapi.social.playlist.application.query.GetPlaylistQuery;
import top.blogapi.social.playlist.application.command.AddSongCommand;
import top.blogapi.social.playlist.application.command.RemoveSongCommand;
import top.blogapi.social.playlist.application.command.VoteSongCommand;
import top.blogapi.social.playlist.application.command.ReorderSongsCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final GetPlaylistQuery getPlaylistQuery;
    private final AddSongCommand addSongCommand;
    private final RemoveSongCommand removeSongCommand;
    private final VoteSongCommand voteSongCommand;
    private final ReorderSongsCommand reorderSongsCommand;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<PlaylistResponse>> getMyPlaylist(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistResponse response = getPlaylistQuery.getMyPlaylist(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PlaylistResponse>> getUserPlaylist(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistResponse response = getPlaylistQuery.getUserPlaylist(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/songs")
    public ResponseEntity<ApiResponse<PlaylistSongResponse>> addSong(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddSongRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistSongResponse response = addSongCommand.execute(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/songs/{songId}")
    public ResponseEntity<ApiResponse<Void>> removeSong(
            @PathVariable Long songId,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        removeSongCommand.execute(principal.getId(), songId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/songs/{songId}/vote")
    public ResponseEntity<ApiResponse<PlaylistSongResponse>> voteSong(
            @PathVariable Long songId,
            @RequestParam int vote,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistSongResponse response = voteSongCommand.execute(principal.getId(), songId, vote);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/songs/reorder")
    public ResponseEntity<ApiResponse<Void>> reorderSongs(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ReorderRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        reorderSongsCommand.execute(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
