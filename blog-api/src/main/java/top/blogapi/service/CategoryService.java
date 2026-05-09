package top.blogapi.service;

import com.github.pagehelper.PageInfo;
import top.blogapi.dto.request.category.CategoryQueryRequest;
import top.blogapi.model.entity.Category;
import top.blogapi.model.vo.BlogTagsInfo;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();

    PageInfo<Category> getCategoryList(CategoryQueryRequest request);

    Category saveCategory(String categoryName);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    boolean categoryExist(String categoryName);

    void deleteCategoryById(Long id);

    void updateCategory(Category category);

    List<BlogTagsInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);
}
