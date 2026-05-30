package top.blogapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.annotation.VisitLogger;
import top.blogapi.dto.request.moment.HandleMomentLike;
import top.blogapi.dto.internal.MomentLikedByGuestIdInternal;
import top.blogapi.dto.response._page.PageResult;
import top.blogapi.dto.response._common.Result;
import top.blogapi.model.enums.VisitBehavior;
import top.blogapi.service.impl.orchestration.MomentOrchestrator;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MomentController {
    MomentOrchestrator momentOrchestrator;

    @VisitLogger(VisitBehavior.MOMENT)
    @GetMapping("/moments")
    public Result<?> listMoments(@RequestParam(defaultValue = "1") Integer pageNum){
        long start = System.currentTimeMillis();

        PageResult<MomentLikedByGuestIdInternal> p =
                momentOrchestrator.getMomentListByPublished( pageNum);

        long end = System.currentTimeMillis();

        System.out.println("API /moments time: " + (end - start) + " ms");
        return Result.ok("Yêu cầu thành công", p);
    }

    @PutMapping("/moment/like")
    public Result<?> likeMoment(@RequestBody HandleMomentLike handleMomentLike){
        momentOrchestrator.handleMomentLike(handleMomentLike);
        return Result.ok("Like!!!");
    }
}
