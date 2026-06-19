package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Badge;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BadgeRepository {

    @Select("SELECT * FROM badges WHERE id = #{id}")
    Optional<Badge> findById(Long id);

    @Select("SELECT * FROM badges ORDER BY tier, name")
    List<Badge> findAll();

    @Insert("""
        INSERT INTO badges (name, display_name, description, icon_url, tier, criteria)
        VALUES (#{name}, #{displayName}, #{description}, #{iconUrl}, #{tier}, #{criteria}::jsonb)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Badge badge);

    @Select("SELECT EXISTS(SELECT 1 FROM badges WHERE name = #{name})")
    boolean existsByName(String name);
}
