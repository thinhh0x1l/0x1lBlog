package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionRepository {

    @Select("SELECT * FROM permissions WHERE id = #{id}")
    Permission findById(Long id);

    @Select("""
        SELECT p.* FROM permissions p
        JOIN role_permissions rp ON p.id = rp.permission_id
        WHERE rp.role_id = #{roleId}
    """)
    List<Permission> findByRoleId(Long roleId);
}
