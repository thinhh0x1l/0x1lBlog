package top.blogapi.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.blogapi.model.entity.Visit;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateVisitExpireFlush {
    Long id;
    int pv;
    LocalDateTime lastActivity;

    public static UpdateVisitExpireFlush from(Visit v){
        return new UpdateVisitExpireFlush(v.getId(), v.getPv(), v.getLastActivity());
    }
}
