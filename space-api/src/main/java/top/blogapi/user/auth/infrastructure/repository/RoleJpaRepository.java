package top.blogapi.user.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.blogapi.user.core.entity.Role;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
