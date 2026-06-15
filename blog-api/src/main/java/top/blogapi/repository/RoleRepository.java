package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface RoleRepository {

    @Select("SELECT * FROM role WHERE id = #{id}")
    Optional<Role> findById(@Param("id") Long id);

    @Select("SELECT * FROM role WHERE name = #{name}")
    Optional<Role> findByName(@Param("name") String name);

    @Select("SELECT r.* FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM role")
    List<Role> findAll();

    @Insert("INSERT INTO role (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Role role);

    @Delete("DELETE FROM role WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
