package top.blogapi.service;

import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.model.entity.Moment;

import java.util.List;

public interface MomentService {
    List<Moment> getMomentList();

    List<Moment> getMomentListByPublished();

    void addLikeByMomentId(Long momentId);

    void updateMomentPublishedById(Long momentId, Boolean published);

    Moment getMomentById(Long id);

    void deleteMomentById(Long id);

    int saveMoment(Moment moment);

    int updateMoment(Moment moment);
}
