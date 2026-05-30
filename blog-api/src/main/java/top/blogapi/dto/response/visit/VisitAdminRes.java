package top.blogapi.dto.response.visit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.blogapi.model.entity.Visit;
import top.blogapi.util.Encryptor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisitAdminRes {
    String guestIdHash;
    String ip;
    String ipSource;
    String os;
    String browser;
    String userAgent;
    int pv;
    LocalDateTime startedAt;
    LocalDateTime lastActivity;
    boolean u;

    public static VisitAdminRes from(Visit v, Long guestId){
        VisitAdminRes var = new VisitAdminRes();
        var.guestIdHash = Encryptor.hashids.encode(v.getGuestId());
        var.ip = v.getIp();
        var.ipSource = v.getIpSource();
        var.os = v.getOs();
        var.browser = v.getBrowser();
        var.pv = v.getPv();
        var.startedAt = v.getStartedAt();
        var.lastActivity = v.getLastActivity();
        var.u = v.getGuestId().equals(guestId);
        return var;
    }
}
