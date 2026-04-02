package top.blogapi.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.model.entity.Moment;
import top.blogapi.model.vo.Result;
import top.blogapi.service.MomentService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class MomentAdminController {
    MomentService momentService;

    @GetMapping("/moments")
    public Result<?> moments(@RequestParam(defaultValue = "1") Integer pageNum){
        String orderBy = "create_time desc";
        PageHelper.startPage(pageNum, 5, orderBy);
        PageInfo<Moment> momentPageInfo =
                 new PageInfo<>(momentService.getMomentList());
        System.out.println(momentPageInfo.toString());
        return Result.ok("Yêu cầu thành công", momentPageInfo);
    }

    @PutMapping("/moment/published")
    public Result<?> updateMomentPublished(Long id, Boolean published){
        momentService.updateMomentPublishedById(id, published);
        return Result.ok("Cập nhật thành công");
    }

}