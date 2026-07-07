package top.blogapi.service.user;

import top.blogapi.model.entity.User;

import java.util.List;

/**
 * Giao diện service quản lý người dùng, cung cấp thao tác CRUD,
 * kiểm tra tính duy nhất và tích hợp cache hồ sơ.
 */
public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User create(User user);
    User update(User user);
    void updateLastActive(Long id);
    boolean existsByEmail(String email);
    List<User> findAll(int page, int size);
    long count();
}
