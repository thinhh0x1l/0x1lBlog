package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Badge;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface BadgeRepository {

    @Select("SELECT * FROM badge WHERE id = #{id}")
    Optional<Badge> findById(@Param("id") Long id);

    @Select("SELECT * FROM badge")
    List<Badge> findAll();

    @Insert("INSERT INTO badge (name, display_name, description, icon_url, tier) VALUES (#{name}, #{displayName}, #{description}, #{iconUrl}, #{tier})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Badge badge);

    @Delete("DELETE FROM badge WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
