package top.blogapi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.About;

import java.util.List;

@Mapper
@Repository
public interface AboutRepository {
    @Select("""
    SELECT * FROM about
""")
    List<About> getList();

    @Update("""
    <script>
        UPDATE about
        SET value = CASE id
        <foreach collection='list' item='item'>
            WHEN #{item.id} THEN #{item.value}
        </foreach>
        END
        WHERE id IN
        <foreach collection='list' item='item' open='(' separator=',' close=')'>
            #{item.id}
        </foreach>
    </script>
""")
    int updateAbout(@Param("list") List<About> list);
}