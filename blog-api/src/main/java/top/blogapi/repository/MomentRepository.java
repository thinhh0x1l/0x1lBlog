package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.dto.internal.MomentInternal;
import top.blogapi.dto.internal.MomentLikesAndLikedInternal;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface MomentRepository {
    @Select("""
        SELECT
           m.id,
           m.content,
           m.create_time,
           m.is_published AS published,
           COUNT(mg.guest_id) AS likes
        FROM moment m
        LEFT JOIN moment_guest mg
           ON m.id = mg.moment_id
        GROUP BY m.id;
""")
    List<MomentInternal> getMomentList();

    @Select("""
        SELECT
           m.id,
           m.content,
           m.create_time
        FROM moment m
        WHERE m.is_published
""")
    List<top.blogapi.model.entity.Moment> getMomentListByPublished();

    @Select("""
    <script>
        SELECT
            mg.moment_id AS id,
            COUNT(mg.moment_id) AS likes,
            EXISTS(
                SELECT 1
                FROM moment_guest mg2
                WHERE mg2.guest_id = #{guestId}
                AND mg2.moment_Id = mg.moment_id
            ) AS liked
        FROM moment_guest mg
        WHERE mg.moment_id IN
        <foreach
            collection='momentIds'
            item='mId'
            open='('
            separator=','
            close=')'
        >
            #{mId}
        </foreach>
        GROUP BY mg.moment_id
    </script>
""")
    List<MomentLikesAndLikedInternal> getMomentLikesAndLikedList(@Param("momentIds") List<Long> momentIds ,
                                                                 Long guestId);

    @Update("""
    INSERT INTO moment_guest (moment_id, guest_id)
    VALUES (#{momentId},#{guestId})
""")
    int addLikeByMomentIdAndGuestId(Long momentId, Long guestId);

    @Delete("""
        DELETE FROM moment_guest
        WHERE moment_id = #{momentId}
        AND   guest_id = #{guestId}
""")
    int deleteLikeByMomentIdAndGuestId(Long momentId, Long guestId);

    @Update("""
    UPDATE moment SET is_published = #{published} WHERE id = #{momentId}
""")
    int updateMomentPublishedById(Long momentId, Boolean published);

    @Select("""
        SELECT
           m.id,
           m.content,
           m.create_time,
           m.is_published AS published,
           COUNT(mg.guest_id) AS likes
        FROM moment m
        LEFT JOIN moment_guest mg
           ON m.id = mg.moment_id
        WHERE m.id = #{id}
        GROUP BY m.id;
""")
    Optional<MomentInternal> getMomentById(Long id);

    @Delete("""
    DELETE FROM moment WHERE id = #{id}
""")
    void deleteMomentById(Long id);

//    @Delete("""
//    <script>
//        DELETE FROM moment_guest
//        WHERE moment_id IN
//        <foreach
//            collection = 'momentIds'
//            item='id'
//            open='('
//            separator=','
//            close=')'
//        >
//            #{id}
//        </foreach>
//    </script>
//""")
//    int deleteMomentGuest(@Param("momentIds") List<Long> momentIds);
//
//    @Delete("""
//    <script>
//        DELETE FROM moment_guest
//        WHERE moment_id IN
//            <foreach
//                collection ='momentIds'
//                item='momentId'
//                open='('
//                separator=','
//                close=')'
//            >
//                #{momentId}
//            </foreach>
//        AND guest_id IN
//            <foreach
//                collection='guestIds'
//                item='guestId'
//                open='('
//                separator=','
//                close=')'
//            >
//                #{guestId}
//            </foreach>
//    </script>
//""")
//    int removeLikeMomentGuest(
//            @Param("momentIds") List<Long> momentIds,
//            @Param("guestIds") List<Long> guestIds);
//

    @Insert("""
    INSERT INTO moment (content, create_time, is_published)
    VALUES (#{content}, #{createTime}, #{published})
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveMoment(top.blogapi.model.entity.Moment moment);

    @Update("""
    UPDATE moment
    SET
        content = #{content},
        create_time = #{createTime},
        is_published = #{published}
    WHERE id = #{id}
""")
    int updateMoment(top.blogapi.model.entity.Moment moment);
}
