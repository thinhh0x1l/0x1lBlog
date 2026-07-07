package top.blogapi.service.playlist.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Playlist;
import top.blogapi.model.entity.PlaylistSong;
import top.blogapi.model.entity.PlaylistVote;
import top.blogapi.repository.PlaylistRepository;
import top.blogapi.repository.PlaylistSongRepository;
import top.blogapi.repository.PlaylistVoteRepository;
import top.blogapi.service.playlist.PlaylistService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai PlaylistService quản lý bài hát, sắp xếp và bỏ phiếu cộng đồng
 * với tự động xóa bài hát khi tổng phiếu đạt -5 hoặc thấp hơn.
 */
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistVoteRepository playlistVoteRepository;

    @Override
    public Playlist findOrCreateByOwnerId(Long ownerId) {
        return playlistRepository.findByOwnerId(ownerId)
                .orElseGet(() -> {
                    Playlist playlist = new Playlist();
                    playlist.setOwnerId(ownerId);
                    playlist.setTitle("My Playlist");
                    playlist.setIsPublic(true);
                    playlist.setSongCount(0);
                    playlistRepository.insert(playlist);
                    return playlist;
                });
    }

    @Override
    public Playlist findById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    @Override
    public Playlist findByOwnerId(Long ownerId) {
        return playlistRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    @Override
    public PlaylistSong findSongById(Long songId) {
        return playlistSongRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_SONG_NOT_FOUND));
    }

    @Override
    public PlaylistSong addSong(PlaylistSong song) {
        song.setSortOrder(playlistSongRepository.nextSortOrder(song.getPlaylistId()));
        song.setVoteCount(0);
        playlistSongRepository.insert(song);
        playlistRepository.incrementSongCount(song.getPlaylistId());
        return song;
    }

    @Override
    public void removeSong(Long songId) {
        PlaylistSong song = findSongById(songId);
        playlistSongRepository.deleteById(songId);
        playlistRepository.decrementSongCount(song.getPlaylistId());
    }

    @Override
    public void reorderSongs(Long playlistId, List<Long> songIds) {
        for (int i = 0; i < songIds.size(); i++) {
            playlistSongRepository.updateSortOrder(songIds.get(i), i);
        }
    }

    @Override
    public PlaylistSong voteSong(Long songId, Long userId, int vote) {
        PlaylistSong song = findSongById(songId);

        Optional<PlaylistVote> existing = playlistVoteRepository.findBySongAndUser(songId, userId);
        if (existing.isPresent()) {
            PlaylistVote voteEntity = existing.get();
            voteEntity.setVote(vote);
            playlistVoteRepository.updateVote(voteEntity);
        } else {
            PlaylistVote voteEntity = new PlaylistVote();
            voteEntity.setPlaylistId(song.getPlaylistId());
            voteEntity.setSongId(songId);
            voteEntity.setUserId(userId);
            voteEntity.setVote(vote);
            playlistVoteRepository.insert(voteEntity);
        }

        int totalVotes = playlistVoteRepository.sumVotesBySongId(songId);
        song.setVoteCount(totalVotes);
        playlistSongRepository.updateVoteCount(songId, totalVotes);

        if (totalVotes <= -5) {
            playlistSongRepository.deleteById(songId);
            playlistRepository.decrementSongCount(song.getPlaylistId());
        }

        return song;
    }

    @Override
    public List<PlaylistSong> getSongsByPlaylistId(Long playlistId) {
        return playlistSongRepository.findByPlaylistIdOrderBySortOrder(playlistId);
    }

    @Override
    public int countSongsByPlaylistId(Long playlistId) {
        return playlistSongRepository.countByPlaylistId(playlistId);
    }

    @Override
    public int countTodaySongsByUserAndPlaylist(Long playlistId, Long userId) {
        return playlistSongRepository.countTodayByPlaylistAndUser(playlistId, userId);
    }

    @Override
    public Optional<PlaylistSong> findSongByPlaylistAndSource(Long playlistId, String source, String sourceId) {
        return playlistSongRepository.findByPlaylistAndSource(playlistId, source, sourceId);
    }
}
