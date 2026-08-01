package top.blogapi.social.playlist.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.entity.PlaylistVote;
import top.blogapi.social.playlist.domain.repository.PlaylistRepository;
import top.blogapi.social.playlist.domain.repository.PlaylistSongRepository;
import top.blogapi.social.playlist.domain.repository.PlaylistVoteRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistVoteRepository playlistVoteRepository;

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

    public Playlist findById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    public Playlist findByOwnerId(Long ownerId) {
        return playlistRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    public PlaylistSong findSongById(Long songId) {
        return playlistSongRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAYLIST_SONG_NOT_FOUND));
    }

    public PlaylistSong addSong(PlaylistSong song) {
        song.setSortOrder(playlistSongRepository.nextSortOrder(song.getPlaylistId()));
        song.setVoteCount(0);
        playlistSongRepository.insert(song);
        playlistRepository.incrementSongCount(song.getPlaylistId());
        return song;
    }

    public void removeSong(Long songId) {
        PlaylistSong song = findSongById(songId);
        playlistSongRepository.deleteById(songId);
        playlistRepository.decrementSongCount(song.getPlaylistId());
    }

    public void reorderSongs(Long playlistId, List<Long> songIds) {
        for (int i = 0; i < songIds.size(); i++) {
            playlistSongRepository.updateSortOrder(songIds.get(i), i);
        }
    }

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

    public List<PlaylistSong> getSongsByPlaylistId(Long playlistId) {
        return playlistSongRepository.findByPlaylistIdOrderBySortOrder(playlistId);
    }

    public int countSongsByPlaylistId(Long playlistId) {
        return playlistSongRepository.countByPlaylistId(playlistId);
    }

    public int countTodaySongsByUserAndPlaylist(Long playlistId, Long userId) {
        return playlistSongRepository.countTodayByPlaylistAndUser(playlistId, userId);
    }

    public Optional<PlaylistSong> findSongByPlaylistAndSource(Long playlistId, String source, String sourceId) {
        return playlistSongRepository.findByPlaylistAndSource(playlistId, source, sourceId);
    }
}
