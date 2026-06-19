package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.AboutInfo;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AboutInfoRepository {

    @Select("SELECT * FROM about_info WHERE id = #{id}")
    Optional<AboutInfo> findById(Long id);

    @Select("SELECT * FROM about_info WHERE type = #{type} ORDER BY updated_at DESC LIMIT 1")
    Optional<AboutInfo> findByType(String type);

    @Select("SELECT * FROM about_info ORDER BY type")
    List<AboutInfo> findAll();

    @Insert("""
        INSERT INTO about_info (content, type) VALUES (#{content}, #{type})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AboutInfo about);

    @Update("""
        UPDATE about_info SET content = #{content}, updated_at = NOW() WHERE id = #{id}
    """)
    int update(AboutInfo about);
}
