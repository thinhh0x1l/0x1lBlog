package top.blogapi.service.user;

import top.blogapi.model.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    User create(User user);
    User update(User user);
    void updateLastActive(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAll(int page, int size);
    long count();
}
