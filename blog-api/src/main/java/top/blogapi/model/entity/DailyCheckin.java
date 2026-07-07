package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bản ghi điểm danh hàng ngày, theo dõi chuỗi ngày và kinh nghiệm thưởng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyCheckin {
    Long id;
    Long userId;
    String checkinDate;
    Integer streakAtTime;
    Integer bonusExp;
    OffsetDateTime createdAt;
}
