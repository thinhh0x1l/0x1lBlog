package top.blogapi.service.reputation;

import java.util.Map;

/**
 * Giao diện service quản lý điểm uy tín người dùng trên các khía cạnh
 * viết lách, cộng đồng, sáng tạo và ảnh hưởng.
 */
public interface ReputationService {
    void addWriting(Long userId, long amount);
    void addCommunity(Long userId, long amount);
    void addCreativity(Long userId, long amount);
    void addInfluence(Long userId, long amount);
    Map<String, Long> getScores(Long userId);
}
