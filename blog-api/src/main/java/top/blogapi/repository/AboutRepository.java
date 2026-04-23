package top.blogapi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}