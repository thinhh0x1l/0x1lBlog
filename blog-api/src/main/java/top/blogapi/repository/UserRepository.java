package top.blogapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.User;

@Repository
@Mapper
public interface UserMapper {
    @Select("""
        SELECT * 
        FROM User u 
        WHERE u.username = #{username}
        LIMIT 1
    """)
    User findByUserName(@Param("username") String userName);
}
