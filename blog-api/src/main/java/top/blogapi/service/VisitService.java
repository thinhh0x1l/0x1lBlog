package top.blogapi.service;


import top.blogapi.model.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {

    Visit createVisit(Visit visit);

    void updateVisitExpire(Long visitId, int pv, LocalDateTime lastActivity);

    List<Visit> getVisitList(int pageNum, int pageSize);

    void insertVisitFlush(List<Visit> insertVisitFlush);
}
