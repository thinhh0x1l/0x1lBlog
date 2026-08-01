package top.blogapi.gamification.reputation.domain.repository;

import top.blogapi.gamification.reputation.domain.entity.UserExpLog;

import java.util.List;

public interface UserExpLogRepository {

    List<UserExpLog> findByUserId(Long userId, int limit, int offset);

    void save(UserExpLog log);

    long sumExpByUserId(Long userId);
}
