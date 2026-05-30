package top.blogapi.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.blogapi.constant.CacheNameConstant;
import top.blogapi.dto.internal.MomentInternal;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Moment;
import top.blogapi.dto.internal.MomentLikesAndLikedInternal;
import top.blogapi.repository.MomentRepository;
import top.blogapi.service.MomentService;
import top.blogapi.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class MomentServiceImpl implements MomentService {
    MomentRepository momentRepository;

    @Override
    public List<MomentInternal> getMomentList() {
        return momentRepository.getMomentList();
    }


    @Cacheable(
            value = CacheNameConstant.MOMENT_LIST,
            key = "#pageNum"
    )
    @Override
    public List<Moment> getMomentListByPublished(Integer pageNum) {
        String orderBy = "create_time desc";
        System.out.println("momentDB");
        PageHelper.startPage(pageNum,5, orderBy);
        return momentRepository.getMomentListByPublished();
    }

    @Override
    public Map<Long, MomentLikesAndLikedInternal> getMomentLikedByGuestIdMap(List<Long> momentIds, Long guestId) {
        return Optional.ofNullable(momentRepository.getMomentLikesAndLikedList(momentIds, guestId))
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(
                        MomentLikesAndLikedInternal::getId,
                        Function.identity()     // m -> m
                ));
    }

    @Override
    public void handleMomentLikeIncrease(Long momentId, Long guestId) {
        momentRepository.addLikeByMomentIdAndGuestId(momentId, guestId);
    }

    @Override
    public void handleMomentLikeDecrease(Long momentId, Long guestId) {
        momentRepository.deleteLikeByMomentIdAndGuestId(momentId, guestId);
    }


    @Override
    public void updateMomentPublishedById(Long momentId, Boolean published) {
        momentRepository.updateMomentPublishedById(momentId,published);
    }

    @Override
    public MomentInternal getMomentById(Long id) {
        return momentRepository.getMomentById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.USER_NOT_FOUND,
                        "MomentInternal với id = " +id+ " không tồn tại!"));

    }

    @Override
    public void deleteMomentById(Long id) {
        // 404 - không tồn tại
        // 403 - không có quyền
        getMomentById(id);
        momentRepository.deleteMomentById(id);
    }

    @Override
    public int saveMoment(Moment moment) {
        momentValid(moment);
        moment.setId(null);
        moment.setCreateTime(LocalDateTime.now());
        return momentRepository.saveMoment(moment);
    }

    @Override
    public int updateMoment(Moment moment) {
        momentValid(moment);
        return momentRepository.updateMoment(moment);
    }

    private void momentValid(Moment moment){
        if(moment == null)
            throw new AppException(ErrorCode.INVALID_INPUT);
        if(moment.getId() !=null)
            getMomentById(moment.getId());
        if(StringUtils.isEmpty(moment.getContent()))
            throw new AppException(ErrorCode.CONTENT_REQUIRED);

    }

}
