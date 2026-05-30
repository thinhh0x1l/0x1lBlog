package top.blogapi.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.internal.MomentInternal;
import top.blogapi.model.entity.Moment;
import top.blogapi.dto.response._common.Result;
import top.blogapi.service.impl.orchestration.MomentOrchestrator;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class MomentAdminController {
    MomentOrchestrator momentOrchestrator;

    @GetMapping("/moments")
    public Result<?> moments(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize){
        String orderBy = "create_time desc";
        PageHelper.startPage(pageNum, pageSize, orderBy);
        PageInfo<MomentInternal> momentPageInfo =
                 new PageInfo<>(momentOrchestrator.getMomentList());
        System.out.println(momentPageInfo.toString());
        return Result.ok("Yêu cầu thành công", momentPageInfo);
    }

    @PutMapping("/moment/published")
    public Result<?> updateMomentPublished(Long id, Boolean published){
        momentOrchestrator.updateMomentPublishedById(id, published);
        return Result.ok("Cập nhật thành công");
    }

    @GetMapping("/moment")
    public Result<?> getMomentById(@RequestParam Long id){
        return Result.ok("Yêu cầu thành công!",momentOrchestrator.getMomentById(id));
    }

    @PostMapping("/moment")
    public Result<?> createMoment(@RequestBody Moment  momentReq){
        return Result.ok("Tạo khoảng khắc thành công!!", momentOrchestrator.saveMoment(momentReq));
    }

    @PutMapping("/moment")
    public Result<?> updateMoment(@RequestBody Moment momentReq){
        return Result.ok("Tạo khoảng khắc thành công!!",momentOrchestrator.updateMoment(momentReq));
    }

    @DeleteMapping("/moment")
    public Result<?> deleteMomentById(@RequestParam Long id){
        momentOrchestrator.deleteMomentById(id);
        return Result.ok("Xóa khoảng khắc thành công!!");
    }
}