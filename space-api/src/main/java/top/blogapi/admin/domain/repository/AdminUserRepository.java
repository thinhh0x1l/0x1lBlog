package top.blogapi.admin.domain.repository;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository {
    Optional<Object[]> findById(Long id);
    List<Object[]> findAll(int page, int size);
    long count();
    void updateRole(Long id, String role);
    void banUser(Long id);
}
