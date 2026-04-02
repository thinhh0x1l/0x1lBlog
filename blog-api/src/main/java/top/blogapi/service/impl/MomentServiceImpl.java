package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.model.entity.Moment;
import top.blogapi.repository.MomentRepository;
import top.blogapi.service.MomentService;

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
}
