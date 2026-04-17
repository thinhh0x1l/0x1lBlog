package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Moment;

import java.util.List;
import java.util.Optional;

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

    @Select("""
    SELECT id, content, create_time, likes, is_published
    FROM moment
    WHERE id = #{id}
""")
    Optional<Moment> getMomentById(Long id);

    @Delete("""
    DELETE FROM moment WHERE id = #{id}
""")
    void deleteMomentById(Long id);

    @Insert("""
    INSERT INTO moment (content, create_time, likes, is_published)
    VALUES (#{content}, #{createTime}, #{likes}, #{published})
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveMoment(Moment moment);

    @Update("""
    UPDATE moment SET content = #{content}, create_time = #{createTime},
                      likes = #{likes}, is_published = #{published}
    WHERE id = #{id}
""")
    int updateMoment(Moment moment);
}
