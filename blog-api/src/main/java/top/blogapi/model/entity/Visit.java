package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Visit {
    Long id;
    Long guestId;
    String ip;
    String ipSource;
    String os;
    String browser;
    String userAgent;
    int pv;
    LocalDateTime startedAt;
    LocalDateTime lastActivity;


    @Override
    public String toString() {
        return "Visit{\n" +
                "id=" + id +  "\n"+
                ", guestId=" + guestId +  "\n"+
                ", ip='" + ip + '\'' +  "\n"+
                ", ipSource='" + ipSource + '\'' +  "\n"+
                ", os='" + os + '\'' +  "\n"+
                ", browser='" + browser + '\'' +  "\n"+
                ", userAgent='" + userAgent + '\'' +  "\n"+
                ", pv=" + pv +  "\n"+
                ", startedAt=" + startedAt +  "\n"+
                ", lastActivity=" + lastActivity +  "\n"+
                '}';
    }
}
