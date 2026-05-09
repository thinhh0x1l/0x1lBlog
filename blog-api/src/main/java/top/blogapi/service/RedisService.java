package top.blogapi.service;


import com.fasterxml.jackson.core.type.TypeReference;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response.category.CategorySlug;
import top.blogapi.model.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface RedisService {
    PageResult<BlogInfo> getBlogInfoPageResultByHash(String hash, Integer pageNum);

    void saveBlogInfoPageResultToHash(String hash, Integer pageNum, Object object);

    <T> List<T> getListByValue(String key, TypeReference<List<T>> typeReference);

    <T> void saveListToValue(String key, List<T> list);

    <T> Map<String, T> getMapByValue(String key, TypeReference<Map<String, T>> typeReference    );

    <T> void saveMapToValue(String key, Map<String, T> map);

    <T> T getObjectByValue(String key, Class<T> t);

    void saveObjectToValue(String key, Object object);
}
