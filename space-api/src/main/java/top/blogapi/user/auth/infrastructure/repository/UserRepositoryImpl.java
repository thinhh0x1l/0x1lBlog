package top.blogapi.user.auth.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import top.blogapi.user.core.entity.User;
import top.blogapi.user.core.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaAdapter;
    private final UserMybatisMapper mybatisAdapter;

    @Override
    public Optional<User> findById(Long id) {
        return jpaAdapter.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaAdapter.findByEmail(email);
    }

    @Override
    public void insert(User user) {
        jpaAdapter.save(user);
    }

    @Override
    public void update(User user) {
        jpaAdapter.save(user);
    }

    @Override
    public void softDelete(Long id) {
        jpaAdapter.softDeleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaAdapter.existsByEmail(email);
    }

    @Override
    public List<User> findAll(int limit, int offset) {
        return jpaAdapter.findAllPaged(PageRequest.of(offset / limit, limit));
    }

    @Override
    public long count() {
        return jpaAdapter.count();
    }

    @Override
    public void updateLastActive(Long id) {
        mybatisAdapter.updateLastActive(id);
    }

    @Override
    public void refreshBlogCount(Long userId) {
        mybatisAdapter.refreshBlogCount(userId);
    }

    @Override
    public void addReputationRaw(Long id, String column, long amount) {
        mybatisAdapter.addReputationRaw(id, column, amount);
    }

    @Override
    public void updateCheckin(Long userId, int streak, LocalDate date) {
        mybatisAdapter.updateCheckin(userId, streak, date);
    }

    @Override
    public Optional<User> findRandomActive() {
        return mybatisAdapter.findRandomActive();
    }

    @Override
    public void addCoins(Long userId, long amount) {
        mybatisAdapter.addCoins(userId, amount);
    }

    @Override
    public void addGems(Long userId, long amount) {
        mybatisAdapter.addGems(userId, amount);
    }

    @Override
    public void deductCoins(Long userId, long amount) {
        mybatisAdapter.deductCoins(userId, amount);
    }

    @Override
    public void deductGems(Long userId, long amount) {
        mybatisAdapter.deductGems(userId, amount);
    }

    @Override
    public void updateGameMode(Long userId, boolean gameMode) {
        mybatisAdapter.updateGameMode(userId, gameMode);
    }

    @Override
    public void updateRole(Long id, String role) {
        mybatisAdapter.updateRole(id, role);
    }

    @Override
    public void banUser(Long id) {
        mybatisAdapter.banUser(id);
    }
}
