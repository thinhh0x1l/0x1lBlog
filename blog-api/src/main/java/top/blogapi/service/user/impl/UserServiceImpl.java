package top.blogapi.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.user.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai UserService với cache, cung cấp CRUD người dùng,
 * kiểm tra tính duy nhất và xóa cache khi cập nhật hồ sơ.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Override
    public User findById(Long id) {
        return cacheService.get(
                CacheKey.user(id),
                User.class,
                () -> userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                CachePolicies.USER_PROFILE
        );
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User create(User user) {
        userRepository.insert(user);
        return user;
    }

    @Override
    public User update(User user) {
        userRepository.update(user);
        User updated = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        cacheService.evict(CacheKey.user(updated.getId()));
        return updated;
    }

    @Override
    public void updateLastActive(Long id) {
        userRepository.updateLastActive(id);
        cacheService.evict(CacheKey.user(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll(int page, int size) {
        return userRepository.findAll(size, page * size);
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}
