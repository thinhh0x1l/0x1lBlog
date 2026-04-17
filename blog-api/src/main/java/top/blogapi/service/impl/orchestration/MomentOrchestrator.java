package top.blogapi.service.impl.orchestration;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Moment;
import top.blogapi.service.MomentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MomentOrchestrator {
    MomentService momentService;

    public List<Moment> getMomentList() {
        return momentService.getMomentList();
    }

    public List<Moment> getMomentListByPublished() {
        return momentService.getMomentListByPublished();
    }

    public void addLikeByMomentId(Long momentId) {
        momentService.addLikeByMomentId(momentId);
    }

    public void updateMomentPublishedById(Long momentId, Boolean published) {
        momentService.updateMomentPublishedById(momentId,published);
    }

    public Moment getMomentById(Long id) {
        return momentService.getMomentById(id);
    }

    public void deleteMomentById(Long id) {
        momentService.deleteMomentById(id);
    }

    public Moment saveMoment(Moment moment) {
        try {
            momentService.saveMoment(moment);
            return momentService.getMomentById(moment.getId());
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getRootCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                throw new AppException(ErrorCode.DATA_CONFLICT);
            }
            throw new AppException(ErrorCode.INTERNAL_ERROR);
        }
    }

    public Moment updateMoment(Moment moment) {
        int affected = momentService.updateMoment(moment);

        if (affected == 0) {
            throw new AppException(ErrorCode.MOMENT_NOT_FOUND);
        }
        return momentService.getMomentById(moment.getId());
    }
}
