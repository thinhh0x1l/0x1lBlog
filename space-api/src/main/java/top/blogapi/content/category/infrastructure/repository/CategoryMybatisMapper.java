package top.blogapi.content.category.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMybatisMapper {

    @Update("UPDATE categories SET blog_count = (SELECT COUNT(*) FROM blogs WHERE category_id = #{categoryId} AND status = 'PUBLISHED' AND deleted_at IS NULL) WHERE id = #{categoryId}")
    void refreshBlogCount(@Param("categoryId") Long categoryId);
}
