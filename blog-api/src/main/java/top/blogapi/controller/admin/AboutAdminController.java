package top.blogapi.controller.admin;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.blogapi.model.entity.About;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.AboutOrchestrator;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class AboutAdminController {
    AboutOrchestrator aboutOrchestrator;

    @GetMapping("/about")
    public Result<?> listAbout(){
        return Result.ok("Yêu cầu thành công!", aboutOrchestrator.aboutSettings());
    }

    @PutMapping("/about")
    public Result<?> updateAbout(@RequestBody List<About> abouts){
        aboutOrchestrator.updateAbouts(abouts);
        return Result.ok("Yêu cầu thành công!!");
    }
}
