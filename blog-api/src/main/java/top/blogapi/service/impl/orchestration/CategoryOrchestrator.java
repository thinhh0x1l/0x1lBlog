package top.blogapi.service.impl.orchestration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.config.RedisKeyConfig;
import top.blogapi.dto.request.category.CategoryQueryRequest;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response.category.CategorySlug;
import top.blogapi.dto.response.category.CategorySlugGetBlogsResponse;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.model.entity.Category;
import top.blogapi.mapper.CategoryMapper;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.CategoryService;
import top.blogapi.service.RedisService;
import top.blogapi.util.SlugUtils;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryOrchestrator {
    CategoryService categoryService;

    CategoryMapper categoryMapper;
    BlogMapper blogMapper;

    RedisService redisService;

    public List<CategoryResponse> getCategoryResponsesList() {
        return categoryService.getCategoryList().stream().map(categoryMapper::toCategoryResponse
        ).toList();
    }

    public PageInfo<?> getCategoryList(CategoryQueryRequest request) {
        return categoryService.getCategoryList(request);
    }

    public List<CategorySlug> getCategoryList(){
        String redisKey = RedisKeyConfig.CATEGORY_NAME_LIST;
        List<CategorySlug> categoryListFromRedis = redisService.getListByValue(redisKey,  new TypeReference<List<CategorySlug>>() {});
        if(categoryListFromRedis == null){
            categoryListFromRedis = categoryService.getCategoryList().stream().map(
                    category -> new CategorySlug(SlugUtils.convertSpaceToHyphen(category.getName()), category.getName())
            ).toList();
            redisService.saveListToValue(redisKey,categoryListFromRedis);
        }
        return categoryListFromRedis;
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

    public CategoryResponse getCategoryByName(String urlName){
        Category category = categoryService.getCategoryByName(SlugUtils.convertHyphenToSpace(urlName));
        return categoryMapper.toCategoryResponse(category);
    }

    public CategorySlugGetBlogsResponse getBlogInfoListByCategoryNameAndIsPublished(String categoryNameSlug,  Integer pageNum, Integer pageSize) {
        String redisHash = RedisKeyConfig.CATEGORY_BLOG_INFO_LIST + categoryNameSlug;

        String categoryName = SlugUtils.convertHyphenToSpace(categoryNameSlug);

        PageResult<BlogInfo> pageResultFromRedis = redisService.getBlogInfoPageResultByHash(redisHash, pageNum);
        if(pageResultFromRedis == null){
            String orderBy = "is_top desc, create_time desc";
            PageHelper.startPage(pageNum,pageSize,orderBy);
            PageInfo<BlogTagsInfo>  blogTagsInfos =
                    new PageInfo<>(categoryService.getBlogInfoListByCategoryNameAndIsPublished(categoryName));
            pageResultFromRedis = PageResult.from(blogTagsInfos.convert(blogMapper::toBlogsResponse));
            redisService.saveBlogInfoPageResultToHash(redisHash, pageNum, pageResultFromRedis);
        }

        return new CategorySlugGetBlogsResponse(
                new CategorySlug(categoryNameSlug, categoryName),
                pageResultFromRedis
        );
    }
    public void updateCategory(Long id, String name) {
        Category category = categoryService.getCategoryById(id);
        category.setName(name);
        categoryService.updateCategory(category);
    }
}
