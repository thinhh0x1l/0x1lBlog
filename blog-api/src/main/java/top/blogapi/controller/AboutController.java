package top.blogapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.AboutOrchestrator;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AboutController {
    AboutOrchestrator aboutOrchestrator;

    @GetMapping("/about")
    public Result<?> about() {
        return Result.ok("Yêu cầu thành công!", aboutOrchestrator.getAboutInfo());
    }
}
