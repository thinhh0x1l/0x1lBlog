package top.blogapi.social.status.domain.repository;

import top.blogapi.social.status.domain.entity.StatusPollVote;

import java.util.List;
import java.util.Optional;

public interface StatusPollVoteRepository {

    Optional<StatusPollVote> findById(Long id);

    List<StatusPollVote> findByPollId(Long pollId);

    Optional<StatusPollVote> findByPollAndUser(Long pollId, Long userId);

    long countByPollAndOption(Long pollId, Integer optionIndex);

    void insert(StatusPollVote vote);

    void delete(Long pollId, Long userId);
}
