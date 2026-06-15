package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Permission;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface PermissionRepository {

    @Select("SELECT * FROM permission WHERE id = #{id}")
    Optional<Permission> findById(@Param("id") Long id);

    @Select("SELECT p.* FROM permission p JOIN role_permission rp ON p.id = rp.permission_id WHERE rp.role_id = #{roleId}")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT * FROM permission")
    List<Permission> findAll();

    @Insert("INSERT INTO permission (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Permission permission);
}
