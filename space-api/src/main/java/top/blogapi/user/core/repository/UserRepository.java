package top.blogapi.user.core.repository;

import top.blogapi.user.core.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    void insert(User user);

    void update(User user);

    void softDelete(Long id);

    boolean existsByEmail(String email);

    List<User> findAll(int limit, int offset);

    long count();

    void updateLastActive(Long id);

    void refreshBlogCount(Long userId);

    void addReputationRaw(Long id, String column, long amount);

    default void addReputation(Long id, String column, long amount) {
        if (!REPUTATION_COLUMNS.contains(column)) {
            throw new IllegalArgumentException("Invalid reputation column: " + column);
        }
        addReputationRaw(id, column, amount);
    }

    Set<String> REPUTATION_COLUMNS = Set.of(
            "reputation_writing", "reputation_community",
            "reputation_creativity", "reputation_influence"
    );

    void updateCheckin(Long userId, int streak, LocalDate date);

    Optional<User> findRandomActive();

    void addCoins(Long userId, long amount);

    void addGems(Long userId, long amount);

    void deductCoins(Long userId, long amount);

    void deductGems(Long userId, long amount);

    void updateGameMode(Long userId, boolean gameMode);

    void updateRole(Long id, String role);

    void banUser(Long id);
}
