package top.blogapi.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.blogapi.annotation.VisitLogger;
import top.blogapi.context.GuestContext;
import top.blogapi.model.entity.Guest;
import top.blogapi.dto.response._common.Result;
import top.blogapi.service.GuestService;
import top.blogapi.service.cacheService.VisitCacheService;

@Component
@Aspect
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class VisitLogAspect {
    VisitCacheService visitCacheService;


    @Pointcut("@annotation(visitLogger)")
    public void logPointCut(VisitLogger visitLogger){
    }

    @Around(
            value = "logPointCut(visitLogger)",
            argNames = "joinPoint,visitLogger"
    )
    public Object process(ProceedingJoinPoint joinPoint, VisitLogger visitLogger) throws Throwable {
        ServletRequestAttributes attrs = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
        if(attrs == null){
            joinPoint.proceed();
            return null;
        }
        Guest guest = GuestContext.get();
        HttpServletRequest request = attrs.getRequest();
        long startTime = System.currentTimeMillis();
        Result<?> result = (Result<?>) joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - startTime);
        System.out.println(visitCacheService.getVisitOrCreate(guest.getId(),request));
        System.out.println(times);
        return result;
    }
}
