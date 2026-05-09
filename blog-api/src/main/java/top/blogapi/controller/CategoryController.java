package top.blogapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.CategoryOrchestrator;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CategoryController {
    CategoryOrchestrator categoryOrchestrator;

    @GetMapping("/categories")
    public Result<?> categories() {
        return Result.ok("Yêu cầu thành công", categoryOrchestrator.getCategoryList());
    }

    @GetMapping("/category/{slug}")
    public Result<?> category(@PathVariable String slug,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "1") Integer pageSize) {
        return Result.ok("Yêu cầu thành công",
                categoryOrchestrator.getBlogInfoListByCategoryNameAndIsPublished(slug,pageNum,pageSize));
    }
}
