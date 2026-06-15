package top.blogapi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.User;

@Repository
@Mapper
public interface UserRepository {
    @Select("""
        SELECT u.id, u.username, u.password, u.nickname, u.avatar, u.email, u.role,
               u.create_time AS created_at, u.update_time AS updated_at
        FROM "user" u
        WHERE u.username = #{username}
        LIMIT 1
    """)
    User findByUserName(@Param("username") String userName);
}
