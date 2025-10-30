package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.entity.Tag;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface TagRepository {
    @Select("SELECT t.id, t.name, t.color FROM tag t ORDER BY id DESC ")
    List<Tag> getTagList();

    @Insert("INSERT INTO tag (name, color) VALUES (#{tag.name}, #{tag.color})")
    @Options(useGeneratedKeys = true, keyProperty = "tag.id")
    int saveTag(@Param("tag") Tag tag);

    @Select("SELECT t.id, t.name, t.color FROM tag t WHERE id = #{id}")
    Optional<Tag> getTagById(@Param("id") Long id);

    @Select("SELECT t.id, t.name, t.color FROM tag t WHERE t.name LIKE #{name}")
    Optional<Tag> getTagByName(String name);
}
