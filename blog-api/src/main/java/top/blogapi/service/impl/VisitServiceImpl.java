package top.blogapi.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.blogapi.constant.CacheNameConstant;
import top.blogapi.model.entity.Visit;
import top.blogapi.repository.VisitRepository;
import top.blogapi.repository.jdbc.FlushJDBC;
import top.blogapi.service.VisitService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class VisitServiceImpl implements VisitService {
    VisitRepository visitRepository;
    FlushJDBC flushJDBC;

    @Override
    public Visit createVisit(Visit visit) {
        int r = visitRepository.createVisit(visit);
        if(r==0)
            log.warn("Tạo mới Visit không thành công!");
        return visit;
    }

    @Override
    public void updateVisitExpire(Long visitId, int pv, LocalDateTime lastActivity) {
        int r = visitRepository.updateVisitExpire(visitId,pv, lastActivity);
        if(r==0)
            log.warn("Update thất bại!");
    }

    @Cacheable(
            cacheNames = CacheNameConstant.VISIT_LIST,
            key = "{#pageNum, #pageSize}"
    )
    @Override
    public List<Visit> getVisitList(int pageNum, int pageSize) {
        String sort= "last_activity desc";
        PageHelper.startPage(pageNum, pageSize, sort);
        return visitRepository.getVisitList();
    }

    @CacheEvict(
            cacheNames = CacheNameConstant.VISIT_LIST,
            allEntries = true
    )
    @Override
    public void insertVisitFlush(List<Visit> insertVisitFlush){
        System.out.println("insertVisitFlush");
        flushJDBC.insertVisit(insertVisitFlush);
    }
}
