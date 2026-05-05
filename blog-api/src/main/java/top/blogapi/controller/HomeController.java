package top.blogapi.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.model.vo.BlogInfo;
import top.blogapi.model.vo.PageResult;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.service.impl.orchestration.SiteSettingOrchestrator;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class HomeController {
    BlogOrchestrator blogOrchestrator;
    SiteSettingOrchestrator siteSettingOrchestrator;

    @GetMapping("/blogs")
    public  Result<?> blogs(@RequestParam(defaultValue = "1") Integer pageNum) {
        try {

            return Result.ok("Yêu cầu thành công", blogOrchestrator.getBlogInfoListByIsPublished(pageNum));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
