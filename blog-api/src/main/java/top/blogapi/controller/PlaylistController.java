package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.request.playlist.AddSongRequest;
import top.blogapi.dto.request.playlist.ReorderRequest;
import top.blogapi.dto.response.PlaylistResponse;
import top.blogapi.dto.response.PlaylistSongResponse;
import top.blogapi.orchestrator.PlaylistOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý danh sách phát cộng tác: tạo, quản lý bài hát, bình chọn và sắp xếp lại.
 */
@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistOrchestrator playlistOrchestrator;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<PlaylistResponse>> getMyPlaylist(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistResponse response = playlistOrchestrator.getMyPlaylist(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PlaylistResponse>> getUserPlaylist(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistResponse response = playlistOrchestrator.getUserPlaylist(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/songs")
    public ResponseEntity<ApiResponse<PlaylistSongResponse>> addSong(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddSongRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        PlaylistSongResponse response = playlistOrchestrator.addSong(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/songs/{songId}")
    public ResponseEntity<ApiResponse<Void>> removeSong(
            @PathVariable Long songId,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        playlistOrchestrator.removeSong(principal.getId(), songId);
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
        PlaylistSongResponse response = playlistOrchestrator.voteSong(principal.getId(), songId, vote);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/songs/reorder")
    public ResponseEntity<ApiResponse<Void>> reorderSongs(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ReorderRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        playlistOrchestrator.reorderSongs(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
