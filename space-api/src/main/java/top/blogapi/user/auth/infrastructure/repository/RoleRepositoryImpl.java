package top.blogapi.user.auth.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.core.entity.Role;
import top.blogapi.user.auth.domain.repository.RoleRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaAdapter;

    @Override
    public Optional<Role> findById(Long id) {
        return jpaAdapter.findById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaAdapter.findByName(name);
    }
}
