package top.blogapi.social.status.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.StatusPollVote;
import top.blogapi.social.status.domain.repository.StatusPollVoteRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StatusPollVoteRepositoryImpl implements StatusPollVoteRepository {

    private final StatusPollVoteJpaRepository jpa;

    @Override
    public Optional<StatusPollVote> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<StatusPollVote> findByPollId(Long pollId) {
        return jpa.findByPollId(pollId);
    }

    @Override
    public Optional<StatusPollVote> findByPollAndUser(Long pollId, Long userId) {
        return jpa.findByPollIdAndUserId(pollId, userId);
    }

    @Override
    public long countByPollAndOption(Long pollId, Integer optionIndex) {
        return jpa.countByPollIdAndOptionIndex(pollId, optionIndex);
    }

    @Override
    public void insert(StatusPollVote vote) {
        if (vote.getCreatedAt() == null) {
            vote.setCreatedAt(Instant.now());
        }
        jpa.save(vote);
    }

    @Override
    public void delete(Long pollId, Long userId) {
        jpa.deleteByPollIdAndUserId(pollId, userId);
    }
}
