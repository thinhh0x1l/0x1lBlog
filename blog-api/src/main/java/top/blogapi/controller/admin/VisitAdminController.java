package top.blogapi.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.response._common.Result;
import top.blogapi.service.impl.orchestration.VisitOrchestrator;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class VisitAdminController {
    VisitOrchestrator visitOrchestrator;

    @GetMapping("/visit")
    public Result<?> getVisitPage(@RequestParam(defaultValue = "1") int pageNum,
                                  @RequestParam(defaultValue = "10")int pageSize){
        return Result.ok("yêu cầu thành công",
                Map.of(
                        "currently", visitOrchestrator.currentVisitList(),
                        "nonCurrently", visitOrchestrator.visitList(pageNum, pageSize)
                ));
    }
}
