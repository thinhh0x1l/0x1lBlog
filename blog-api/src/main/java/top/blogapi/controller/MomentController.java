package top.blogapi.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.moment.HandleMomentLike;
import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.model.vo.MomentLikedByGuestId;
import top.blogapi.model.vo.PageResult;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.MomentOrchestrator;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MomentController {
    MomentOrchestrator momentOrchestrator;

    @GetMapping("/moments")
    public Result<?> listMoments(@RequestParam(defaultValue = "1") Integer pageNum,
                                 HttpServletRequest request){
        long start = System.currentTimeMillis();

        PageResult<MomentLikedByGuestId> p =
                momentOrchestrator.getMomentListByPublished(request, pageNum);

        long end = System.currentTimeMillis();

        System.out.println("API /moments time: " + (end - start) + " ms");
        return Result.ok("Yêu cầu thành công", p);
    }

    @PutMapping("/moment/like")
    public Result<?> likeMoment(@RequestBody HandleMomentLike handleMomentLike, HttpServletRequest request){
        momentOrchestrator.handleMomentLike(handleMomentLike, request);
        return Result.ok("Like!!!");
    }
}
