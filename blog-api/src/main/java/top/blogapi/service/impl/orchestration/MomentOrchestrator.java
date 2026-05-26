package top.blogapi.service.impl.orchestration;


import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.constant.HeaderConstant;
import top.blogapi.dto.request.moment.HandleMomentLike;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Guest;
import top.blogapi.model.entity.Moment;
import top.blogapi.model.vo.MomentLikedByGuestId;
import top.blogapi.model.vo.MomentLikesAndLiked;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.GuestService;
import top.blogapi.service.MomentService;
import top.blogapi.util.markdown.MarkdownUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MomentOrchestrator {
    MomentService momentService;
    GuestService guestService;

    public List<top.blogapi.model.vo.Moment> getMomentList() {
        return momentService.getMomentList();
    }

    public PageResult<MomentLikedByGuestId> getMomentListByPublished(HttpServletRequest request,
                                                                     Integer pageNum) {
        Long guestId = guestService.getGuestOrCreateByToken(
                (String)request.getAttribute(HeaderConstant.GUEST_HEADER)).getId();
        List<Moment> ms = momentService.getMomentListByPublished(pageNum);
        List<Long>  momentIds = ms.stream().map(Moment::getId).toList();
        Map<Long, MomentLikesAndLiked> map = momentService.getMomentLikedByGuestIdMap(momentIds,guestId);
        PageInfo<MomentLikedByGuestId> result =
                new PageInfo<>(ms).convert(
                        m -> MomentLikedByGuestId.builder()
                                .id(m.getId())
                                .createTime(m.getCreateTime())
                                .content(MarkdownUtils.markdownToHtmlExtensions(m.getContent()))
                                .likes(map.getOrDefault(m.getId(), MomentLikesAndLiked.empty).getLikes())
                                .liked(map.getOrDefault(m.getId(), MomentLikesAndLiked.empty).isLiked())
                                .build()
                );
        return PageResult.from(result);
    }

    public void handleMomentLike(HandleMomentLike handleMomentLike, HttpServletRequest request) {
        System.out.println("{{Debug}}: "+handleMomentLike.toString());
        Guest guest = guestService.getGuestOrCreateByToken((String)request.getAttribute(HeaderConstant.GUEST_HEADER));
        if(handleMomentLike.getLiked()>0)
            momentService.handleMomentLikeIncrease(handleMomentLike.getId(),guest.getId());
        else
            momentService.handleMomentLikeDecrease(handleMomentLike.getId(),guest.getId());
    }

    public void updateMomentPublishedById(Long momentId, Boolean published) {
        momentService.updateMomentPublishedById(momentId,published);
    }

    public top.blogapi.model.vo.Moment getMomentById(Long id) {
        return momentService.getMomentById(id);
    }

    public void deleteMomentById(Long id) {
        momentService.deleteMomentById(id);
    }

    public Moment saveMoment(Moment moment) {
        try {
            momentService.saveMoment(moment);
            return moment;
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
        return moment;
    }
}
