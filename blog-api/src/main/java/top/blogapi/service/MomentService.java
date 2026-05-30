package top.blogapi.service;

import top.blogapi.dto.internal.MomentInternal;
import top.blogapi.model.entity.Moment;
import top.blogapi.dto.internal.MomentLikesAndLikedInternal;

import java.util.List;
import java.util.Map;

public interface MomentService {
    List<MomentInternal> getMomentList();

    List<Moment> getMomentListByPublished(Integer pageNum);

    Map<Long, MomentLikesAndLikedInternal> getMomentLikedByGuestIdMap(List<Long> momentIds, Long guestId);

    void handleMomentLikeIncrease(Long momentId, Long guestId);

    void handleMomentLikeDecrease(Long momentId, Long guestId);

    void updateMomentPublishedById(Long momentId, Boolean published);

    MomentInternal getMomentById(Long id);

    void deleteMomentById(Long id);

    int saveMoment(Moment moment);

    int updateMoment(Moment moment);
}
