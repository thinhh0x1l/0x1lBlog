package top.blogapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.blogapi.bean.User;

@Repository
@Mapper
public interface UserDao {
    @Select("""
        SELECT * 
        FROM user u 
        WHERE u.username = #{username}
        LIMIT 1
    """)
    User findByUserName(@Param("username") String userName);
}
