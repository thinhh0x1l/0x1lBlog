package top.blogapi.user.auth.domain.repository;

import top.blogapi.user.core.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);
}
