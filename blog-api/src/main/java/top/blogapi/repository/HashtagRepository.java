package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Hashtag;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code hashtags}. Cung cấp truy vấn tra cứu
 * thẻ, thẻ xu hướng và quản lý số lần sử dụng.
 */
@Mapper
public interface HashtagRepository {

    @Select("SELECT * FROM hashtags WHERE id = #{id}")
    Optional<Hashtag> findById(Long id);

    @Select("SELECT * FROM hashtags WHERE name = #{name}")
    Optional<Hashtag> findByName(String name);

    @Select("SELECT * FROM hashtags ORDER BY usage_count DESC LIMIT #{limit}")
    List<Hashtag> findTop(@Param("limit") int limit);

    @Insert("INSERT INTO hashtags (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hashtag hashtag);

    @Update("UPDATE hashtags SET usage_count = usage_count + 1 WHERE id = #{id}")
    int incrementUsage(Long id);

    @Update("UPDATE hashtags SET usage_count = usage_count - 1 WHERE id = #{id}")
    int decrementUsage(Long id);

    @Select("SELECT EXISTS(SELECT 1 FROM hashtags WHERE name = #{name})")
    boolean existsByName(String name);
}
