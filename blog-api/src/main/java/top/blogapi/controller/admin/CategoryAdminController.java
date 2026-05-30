package top.blogapi.controller.admin;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.category.CategoryQueryRequest;
import top.blogapi.service.impl.orchestration.CategoryOrchestrator;
import top.blogapi.dto.response._common.Result;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class CategoryAdminController {
    CategoryOrchestrator categoryOrchestrator;

    @GetMapping("/categories")
    public Result<?> categories(@ModelAttribute CategoryQueryRequest request) {
        return Result.ok("Yêu cầu thành công",categoryOrchestrator.getCategoryList(request));
    }

    @DeleteMapping("/category/{id}")
    public Result<?> deleteCategoryById(@PathVariable Long id) {
        categoryOrchestrator.deleteCategoryById(id);
        return Result.ok("Xóa thể loại thành công");
    }

    @PostMapping("/categories")
    public Result<?> addCategory(@RequestParam(value = "name", required = true) String name) {
        categoryOrchestrator.createCategory(name);
        return Result.ok("Thêm thể loại thành công [ "+ name +" ]" );
    }


    @PutMapping("/category/{id}")
    public Result<?> updateCategoryById(@PathVariable Long id,
                                        @RequestParam(value = "name", required = true) String name) {
        categoryOrchestrator.updateCategory(id, name);
        return Result.ok("Cập nhật thể loại thành công !!");
    }

}
