package top.blogapi.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "users", key = "'findById:' + #id")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "users", key = "'findByUsername:' + #username")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        userRepository.insert(user);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User update(User user) {
        userRepository.update(user);
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void updateLastActive(Long id) {
        userRepository.updateLastActive(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
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
