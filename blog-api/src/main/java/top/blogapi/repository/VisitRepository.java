package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface VisitRepository {
    @Insert("""
        INSERT INTO visit (
            guest_id,
            ip,
            ip_source,
            os,
            browser,
            user_agent,
            pv,
            started_at,
            last_activity
        )values(
            #{guestId},
            #{ip},
            #{ipSource},
            #{os},
            #{browser},
            #{userAgent},
            #{pv},
            #{startedAt},
            #{lastActivity}
        )
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createVisit(Visit visit);

    @Update("""
        UPDATE visit
        SET pv = #{pv},
            last_activity = #{lastActivity}
        WHERE id = #{visitId}
""")
    int updateVisitExpire(Long visitId, int pv, LocalDateTime lastActivity);

    @Select("""
        SELECT
            id,
            guest_id,
            ip,
            ip_source,
            os,
            browser,
            user_agent,
            started_at,
            last_activity,
            pv
        FROM visit
""")
    List<Visit> getVisitList();
}
