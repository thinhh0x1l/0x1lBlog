package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.model.entity.Visit;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VisitDto {
    Long guestId;
    String ip;
    String ipSource;
    String os;
    String browser;
    String userAgent;
    AtomicLong pv;
    LocalDateTime startedAt;
    LocalDateTime lastActivity;

    Visit visit;

    public Visit getVisit(){
        if(visit != null){
            visit.setPv(pv == null ? visit.getPv() : (int) pv.get());
            visit.setLastActivity(lastActivity);
            return visit;
        }
        return visit = new Visit(
                null,
                guestId,
                ip,
                ipSource,
                os,
                browser,
                userAgent,
                pv == null ? 1 : (int) pv.get(),
                startedAt,
                lastActivity
        );
    }
}
