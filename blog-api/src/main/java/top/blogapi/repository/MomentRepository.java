package top.blogapi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Moment;

import java.util.List;

@Mapper
@Repository
public interface MomentRepository {
    @Select("""
    SELECT id, content, likes, is_published AS published, create_time
    FROM moment
""")
    List<Moment> getMomentList();

    @Select("""
    SELECT id, content, likes, create_time
    FROM moment
    WHERE is_published
""")
    List<Moment> getMomentListByPublished();

    @Update("""
    UPDATE moment SET likes = likes + 1 WHERE id = #{momentId}
""")
    int addLikeByMomentId(Long momentId);

    @Update("""
    UPDATE moment SET is_published = #{published} WHERE id = #{momentId}
""")
    int updateMomentPublishedById(Long momentId, Boolean published);
}
