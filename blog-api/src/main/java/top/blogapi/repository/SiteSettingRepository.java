package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.SiteSetting;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SiteSettingRepository {

    @Select("SELECT * FROM site_settings WHERE id = #{id}")
    Optional<SiteSetting> findById(Long id);

    @Select("SELECT * FROM site_settings WHERE key = #{key}")
    Optional<SiteSetting> findByKey(String key);

    @Select("SELECT * FROM site_settings ORDER BY key")
    List<SiteSetting> findAll();

    @Insert("""
        INSERT INTO site_settings (key, value, type, description)
        VALUES (#{key}, #{value}, #{type}, #{description})
        ON CONFLICT (key) DO UPDATE SET value = #{value}, updated_at = NOW()
    """)
    int upsert(SiteSetting setting);

    @Delete("DELETE FROM site_settings WHERE id = #{id}")
    int delete(Long id);
}
