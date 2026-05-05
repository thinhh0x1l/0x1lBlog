package top.blogapi.service.impl.orchestration;

import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.category.CategoryQueryRequest;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.model.entity.Category;
import top.blogapi.mapper.CategoryMapper;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryOrchestrator {
    CategoryService categoryService;

    CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategoryResponsesList() {
        return categoryService.getCategoryList().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    public PageInfo<?> getCategoryList(CategoryQueryRequest request) {
        return categoryService.getCategoryList(request);
    }

    public List<Category> getCategoryList(){
        return categoryService.getCategoryList();
    }

    public void deleteCategoryById(Long id) {
//        if(blogService.countBlogByCategoryId(id)>0)
//            throw CategoryServiceException.builder()
//                    .categoryExistBlogs("CATEGORY","Thể vẫn tồn tại các Blog")
//                    .build();
//        if(categoryService.deleteCategoryById(id)==0)
//            throw CategoryServiceException.builder()
//                    .blogDoesntAddCategories("CATEGORY", HttpStatus.INTERNAL_SERVER_ERROR,"Xóa Blog không thành công !!!")
//                    .errorCode("DELETE_BLOG_UNSUCCESSFUL")
//                    .build();
    }

    public void createCategory(String name) {
        categoryService.saveCategory(name);
    }

    public void updateCategory(Long id, String name) {
        Category category = categoryService.getCategoryById(id);
        category.setName(name);
        categoryService.updateCategory(category);
    }
}
