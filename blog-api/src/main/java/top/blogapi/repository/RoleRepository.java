package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Role;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code roles}. Cung cấp tra cứu vai trò theo ID
 * và tên để phân quyền.
 */
@Mapper
public interface RoleRepository {

    @Select("SELECT * FROM roles WHERE id = #{id}")
    Optional<Role> findById(Long id);

    @Select("SELECT * FROM roles WHERE name = #{name}")
    Optional<Role> findByName(String name);
}
