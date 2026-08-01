package top.blogapi.engagement.share.domain.repository;

import top.blogapi.engagement.share.domain.entity.Share;

import java.util.List;

public interface ShareRepository {

    void insert(Share share);

    List<Share> findByTarget(String targetType, Long targetId, int limit, int offset);

    long countByTarget(String targetType, Long targetId);
}
