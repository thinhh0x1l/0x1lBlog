package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.DailyCheckin;

import java.time.LocalDate;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code daily_checkins}. Theo dõi điểm danh hàng
 * ngày, chuỗi điểm danh và kinh nghiệm thưởng.
 */
@Mapper
public interface DailyCheckinRepository {

    @Select("SELECT * FROM daily_checkins WHERE user_id = #{userId} AND checkin_date = #{checkinDate}")
    Optional<DailyCheckin> findByUserAndDate(Long userId, LocalDate checkinDate);

    @Select("SELECT * FROM daily_checkins WHERE user_id = #{userId} ORDER BY checkin_date DESC LIMIT 1")
    Optional<DailyCheckin> findLastByUserId(Long userId);

    @Insert("INSERT INTO daily_checkins (user_id, checkin_date, streak_at_time, bonus_exp) VALUES (#{userId}, #{checkinDate}, #{streakAtTime}, #{bonusExp})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DailyCheckin checkin);

    @Select("SELECT COUNT(*) FROM daily_checkins WHERE user_id = #{userId}")
    long countByUserId(Long userId);
}
