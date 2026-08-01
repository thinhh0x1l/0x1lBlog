package top.blogapi.user.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.user.core.entity.User;
import top.blogapi.user.core.repository.UserRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    public User findById(Long id) {
        return cacheService.get(
                CacheKey.user(id),
                User.class,
                () -> userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                CachePolicies.USER_PROFILE
        );
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User create(User user) {
        userRepository.insert(user);
        return user;
    }

    public User update(User user) {
        userRepository.update(user);
        User updated = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        cacheService.evict(CacheKey.user(updated.getId()));
        return updated;
    }

    public void updateLastActive(Long id) {
        userRepository.updateLastActive(id);
        cacheService.evict(CacheKey.user(id));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll(int page, int size) {
        return userRepository.findAll(size, page * size);
    }

    public long count() {
        return userRepository.count();
    }

    public void updateRole(Long userId, String role) {
        userRepository.updateRole(userId, role);
        cacheService.evict(CacheKey.user(userId));
    }

    public void banUser(Long userId) {
        userRepository.banUser(userId);
        cacheService.evict(CacheKey.user(userId));
    }
}
