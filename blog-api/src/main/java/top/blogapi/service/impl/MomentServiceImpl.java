package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Moment;
import top.blogapi.repository.MomentRepository;
import top.blogapi.service.MomentService;
import top.blogapi.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class MomentServiceImpl implements MomentService {
    MomentRepository momentRepository;

    @Override
    public List<Moment> getMomentList() {
        return momentRepository.getMomentList();
    }

    @Override
    public List<Moment> getMomentListByPublished() {
        return momentRepository.getMomentListByPublished();
    }

    @Override
    public void addLikeByMomentId(Long momentId) {
        momentRepository.addLikeByMomentId(momentId);
    }

    @Override
    public void updateMomentPublishedById(Long momentId, Boolean published) {
        momentRepository.updateMomentPublishedById(momentId,published);
    }

    @Override
    public Moment getMomentById(Long id) {
        return momentRepository.getMomentById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.USER_NOT_FOUND,
                        "Moment với id = " +id+ " không tồn tại!"));

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
