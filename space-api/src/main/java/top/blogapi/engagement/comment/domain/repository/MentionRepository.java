package top.blogapi.engagement.comment.domain.repository;

import top.blogapi.engagement.comment.domain.entity.Mention;

import java.util.List;

public interface MentionRepository {

    void insert(Mention mention);

    List<Mention> findByTargetUserId(Long userId, int limit, int offset);
}
