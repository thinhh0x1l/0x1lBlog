package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.UserExpLog;

import java.util.List;

@Mapper
public interface UserExpLogRepository {

    @Select("SELECT * FROM user_exp_log WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<UserExpLog> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("INSERT INTO user_exp_log (user_id, amount, reason, ref_id) VALUES (#{userId}, #{amount}, #{reason}, #{refId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserExpLog log);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM user_exp_log WHERE user_id = #{userId}")
    long sumExpByUserId(Long userId);
}
