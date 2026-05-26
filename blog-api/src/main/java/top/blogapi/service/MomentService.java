package top.blogapi.service;

import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.model.entity.Moment;
import top.blogapi.model.vo.MomentLikedByGuestId;
import top.blogapi.model.vo.MomentLikesAndLiked;

import java.util.List;
import java.util.Map;

public interface MomentService {
    List<top.blogapi.model.vo.Moment> getMomentList();

    List<Moment> getMomentListByPublished(Integer pageNum);

    Map<Long, MomentLikesAndLiked> getMomentLikedByGuestIdMap(List<Long> momentIds, Long guestId);

    void handleMomentLikeIncrease(Long momentId, Long guestId);

    void handleMomentLikeDecrease(Long momentId, Long guestId);

    void updateMomentPublishedById(Long momentId, Boolean published);

    top.blogapi.model.vo.Moment getMomentById(Long id);

    void deleteMomentById(Long id);

    int saveMoment(Moment moment);

    int updateMoment(Moment moment);
}
