package top.blogapi.social.playlist.domain.repository;

import top.blogapi.social.playlist.domain.entity.PlaylistVote;

import java.util.List;
import java.util.Optional;

public interface PlaylistVoteRepository {

    Optional<PlaylistVote> findBySongAndUser(Long songId, Long userId);

    List<PlaylistVote> findBySongId(Long songId);

    void insert(PlaylistVote vote);

    void updateVote(PlaylistVote vote);

    void deleteById(Long id);

    int sumVotesBySongId(Long songId);
}
