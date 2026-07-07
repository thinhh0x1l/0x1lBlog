package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Permission;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code permissions}. Cung cấp tra cứu theo ID
 * và kết nối qua role_permissions để tìm quyền cho vai trò.
 */
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
