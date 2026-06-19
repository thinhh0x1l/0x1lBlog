package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Role;

import java.util.Optional;

@Mapper
public interface RoleRepository {

    @Select("SELECT * FROM roles WHERE id = #{id}")
    Optional<Role> findById(Long id);

    @Select("SELECT * FROM roles WHERE name = #{name}")
    Optional<Role> findByName(String name);
}
