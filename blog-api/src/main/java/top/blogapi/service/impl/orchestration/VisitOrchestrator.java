package top.blogapi.service.impl.orchestration;


import com.github.pagehelper.PageInfo;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.constant.HeaderConstant;
import top.blogapi.context.GuestContext;
import top.blogapi.dto.internal.UpdateVisitExpireFlush;
import top.blogapi.dto.internal.UserAgentDTO;
import top.blogapi.dto.internal.VisitDto;
import top.blogapi.dto.response._page.PageResult;
import top.blogapi.dto.response.visit.VisitAdminRes;
import top.blogapi.model.entity.Guest;
import top.blogapi.model.entity.Visit;
import top.blogapi.repository.jdbc.FlushJDBC;
import top.blogapi.service.VisitService;
import top.blogapi.service.cacheService.VisitCacheService;
import top.blogapi.service.impl.GeoIpService;
import top.blogapi.util.IpAddressUtils;
import top.blogapi.util.UserAgentUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VisitOrchestrator {
    VisitCacheService visitCacheService;
    VisitService visitService;


    public void updateVisitExpire(Visit visit){
        System.out.println("updateVisit");
        visitService.updateVisitExpire(visit.getId(), visit.getPv(), visit.getLastActivity());
    }

    public PageResult<VisitAdminRes> visitList(int pageNum, int pageSize){
        Long guestId = GuestContext.getId();
        PageInfo<Visit> visitPageInfo =
                new PageInfo<>(visitService.getVisitList(pageNum,pageSize));
        PageInfo<VisitAdminRes> visitAdminResPageInfo =
                visitPageInfo.convert(v -> VisitAdminRes.from(v,guestId));
        return PageResult.from(visitAdminResPageInfo);
    }

    public List<VisitAdminRes> currentVisitList(){
        Long guestId = GuestContext.getId();
        List<VisitAdminRes> result = new ArrayList<>();
        // snapshot queue

        Map<Long, VisitDto> cacheSnapshot =
                new HashMap<>(visitCacheService.getVisitCache().asMap());
        List<VisitDto> expiresSnapshot =
                new ArrayList<>(visitCacheService.getVisitExpires());

        // snapshot cache

        expiresSnapshot.forEach(v ->
                result.add(VisitAdminRes.from(v.getVisit(), guestId)));

        cacheSnapshot.values().forEach(v ->
                result.add(VisitAdminRes.from(v.getVisit(), guestId)));
        return result;
    }

}
