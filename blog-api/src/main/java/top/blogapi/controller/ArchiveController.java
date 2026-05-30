package top.blogapi.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.dto.response._common.Result;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ArchiveController {
    BlogOrchestrator blogOrchestrator;

    @GetMapping("/archives")
    public Result<?> archives() {
        try {
            return Result.ok("Yêu cầu thành công !", blogOrchestrator.getArchiveBlogListIsPublished());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
