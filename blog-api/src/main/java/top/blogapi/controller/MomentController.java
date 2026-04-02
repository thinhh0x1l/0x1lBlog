package top.blogapi.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.response.moment.MomentPublished;
import top.blogapi.model.vo.Result;
import top.blogapi.service.MomentService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MomentController {
    MomentService momentService;


    @GetMapping("/moments")
    public Result<?> listMoments(@RequestParam(defaultValue = "1") Integer pageNum){
        String orderBy = "create_time desc";
        PageHelper.startPage(pageNum,5, orderBy);
        PageInfo<MomentPublished> momentPublishedPageInfo = new PageInfo<>(momentService.getMomentListByPublished()).convert(m ->
            new MomentPublished(m.getId(),m.getContent(),m.getCreateTime(),m.getLikes()));
        return Result.ok("Yêu cầu thành công", momentPublishedPageInfo);
    }

    @PutMapping("/moments/like")
    public Result<?> likeMoment(@RequestParam Long momentId){
        momentService.addLikeByMomentId(momentId);
        return Result.ok("Like!!!");
    }
}
