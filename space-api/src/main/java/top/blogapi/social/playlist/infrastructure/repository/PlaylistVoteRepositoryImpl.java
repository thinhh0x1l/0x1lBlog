package top.blogapi.social.playlist.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.PlaylistVote;
import top.blogapi.social.playlist.domain.repository.PlaylistVoteRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaylistVoteRepositoryImpl implements PlaylistVoteRepository {

    private final PlaylistVoteJpaRepository jpa;
    private final PlaylistVoteMybatisMapper mybatis;

    @Override
    public Optional<PlaylistVote> findBySongAndUser(Long songId, Long userId) {
        return jpa.findBySongIdAndUserId(songId, userId);
    }

    @Override
    public List<PlaylistVote> findBySongId(Long songId) {
        return jpa.findBySongId(songId);
    }

    @Override
    public void insert(PlaylistVote vote) {
        if (vote.getCreatedAt() == null) {
            vote.setCreatedAt(Instant.now());
        }
        jpa.save(vote);
    }

    @Override
    public void updateVote(PlaylistVote vote) {
        jpa.updateVote(vote.getId(), vote.getVote());
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public int sumVotesBySongId(Long songId) {
        return mybatis.sumVotesBySongId(songId);
    }
}
